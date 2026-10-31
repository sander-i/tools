package org.tools.encrypt.symmetrical.des;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Des;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * DES 对称加解密工具。
 *
 * <p>默认算法模式为 {@code DES}（即 JCA 默认的 {@code DES/ECB/PKCS5Padding}，此时 IV 被忽略）；
 * 密钥为原始字符串，按 UTF-8 取字节后必须恰好 8 字节。明文按 UTF-8 编码，密文为 Base64。</p>
 *
 * <p>线程安全：{@code load} 与 {@code encode}/{@code decode} 并发不安全（静态全局配置）；
 * 多次 {@code encode}/{@code decode} 之间线程安全（每次调用新建 Cipher）。</p>
 *
 * @author sander 2024/04/05
 * @since 1.0.6.1
 */
@Slf4j
public class DesHelper {
    @Getter
    private static DesProperties properties;

    private DesHelper() {
    }

    private static void init(Des des, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(des.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(des.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(des.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(des.getSignAlgorithm());
        }
    }

    /**
     * 按分组名装载 DES 配置（实体需为 Des，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws DesException             配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws DesException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DesException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Des)) {
            throw new DesException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Des ！");
        }
        Des des = (Des) keyPair;
        if (!des.getEnabled()) {
            throw new DesException("Des enable: false");
        }
        if (des.getGroupEnabled()) {
            List<KeyPair> groupList = des.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(des, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    /**
     * 装载 KeyPair 实体配置（密钥为原始字符串，UTF-8 编码后恰好 8 字节）
     *
     * @param keyPair 密钥对配置
     * @throws DesException             配置为空或密钥非法时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws DesException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new DesException("keyPair 不能为空！");
        }
        DesProperties properties = new DesProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        SecretKey secretKey = DesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    /**
     * 装载 DES 配置（仅有字符串密钥时自动按 UTF-8 原文构造 SecretKey）
     *
     * @param properties DES 配置
     * @throws DesException 配置或密钥为空时抛出
     */
    public static void load(DesProperties properties) throws DesException {
        if (BaseUtils.isEmpty(properties)) {
            throw new DesException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            // 仅提供字符串密钥时，按 DES 约定（UTF-8 原文，8 字节）构造密钥对象
            if (BaseUtils.isNotEmpty(properties.getSecretKeyStr())) {
                properties.setDesKeyFixedLength();
            }
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new DesException("secretKey 不能为空！");
        }
        DesHelper.properties = properties;
    }

    /**
     * 使用全局激活配置加密
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws DesException 密钥/模式非法或 JCE 操作失败时抛出
     */
    public static String encode(String raw) throws DesException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 使用指定字符串密钥加密（UTF-8 原文，须恰好 8 字节）
     *
     * @param secretKey 密钥字符串
     * @param raw       明文
     * @return Base64 密文
     * @throws DesException 密钥非法或加密失败时抛出
     */
    public static String encode(String secretKey, String raw) throws DesException {
        SecretKey key;
        key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 使用指定密钥对象加密
     *
     * @param secretKey DES SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws DesException 密钥非法或加密失败时抛出
     */
    public static String encode(SecretKey secretKey, String raw) throws DesException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 使用全局激活配置解密
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws DesException 密文/密钥非法或解密失败时抛出
     */
    public static String decode(String wrought) throws DesException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 使用指定字符串密钥解密
     *
     * @param secretKey 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws DesException 密钥非法或解密失败时抛出
     */
    public static String decode(String secretKey, String wrought) throws DesException {
        SecretKey key;
        key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 使用指定密钥对象解密
     *
     * @param secretKey DES SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws DesException 密钥非法或解密失败时抛出
     */
    public static String decode(SecretKey secretKey, String wrought) throws DesException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new DesException(e);
        }
    }

    /**
     * 取出被包装的原始异常，避免异常链多套一层
     *
     * @param e 模块异常
     * @return 原始异常；无 cause 时返回自身
     */
    private static Exception unwrap(SymmetricalException e) {
        return e.getCause() instanceof Exception ? (Exception) e.getCause() : e;
    }
}
