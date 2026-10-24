package org.tools.encrypt.symmetrical.des;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Des;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

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
 */
public class DesHelper {

    private DesHelper() {
    }

    @Getter
    private static DesProperties properties;

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

    /** 按分组名装载 DES 配置（实体需为 Des，缺省字段由顶层配置补齐） */
    public static void load(KeyPair keyPair, String name) throws DesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
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

    /** 装载 KeyPair 实体配置（密钥为原始字符串，UTF-8 编码后恰好 8 字节） */
    public static void load(KeyPair keyPair) throws DesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
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

    /** 装载 DES 配置（仅有字符串密钥时自动按 UTF-8 原文构造 SecretKey） */
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

    /** 使用全局激活配置加密 */
    public static String encode(String raw) throws DesException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    /** 使用指定字符串密钥加密（UTF-8 原文，须恰好 8 字节） */
    public static String encode(String secretKey, String raw) throws DesException {
        SecretKey key;
        key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    /** 使用指定密钥对象加密 */
    public static String encode(SecretKey secretKey, String raw) throws DesException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    /** 使用全局激活配置解密 */
    public static String decode(String wrought) throws DesException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    /** 使用指定字符串密钥解密 */
    public static String decode(String secretKey, String wrought) throws DesException {
        SecretKey key;
        key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    /** 使用指定密钥对象解密 */
    public static String decode(SecretKey secretKey, String wrought) throws DesException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
