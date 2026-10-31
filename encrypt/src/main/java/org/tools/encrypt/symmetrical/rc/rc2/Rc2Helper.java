package org.tools.encrypt.symmetrical.rc.rc2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc2;
import org.tools.encrypt.entity.symmetrical.Rc2Properties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.rc.rc2.Rc2Exception;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.SecretKey;

/**
 * RC2 对称加解密工具。
 *
 * <p>默认算法模式为 {@code RC2}（即 JCA 默认的 {@code RC2/ECB/PKCS5Padding}，此时 IV 被忽略）；
 * {@code RC2/CBC/PKCS5Padding} 模式下 IV 按 UTF-8 取字节后必须为 8 字节。密钥为 Hex 字符串
 * （16 字节即 128 位，经 {@link Rc2KeyGenerator#getSecretKey(String, String)} 解码）。
 * 明文按 UTF-8 编码，密文为 Base64。</p>
 *
 * <p>本类基于静态全局配置工作：{@code load} 替换进程内唯一的激活配置，
 * 因此 {@code load} 与 {@code encode}/{@code decode} 并发调用不安全；
 * 多次 {@code encode}/{@code decode} 之间线程安全（每次调用新建 Cipher）。
 * 多组密钥请使用 {@link #load(KeyPair, String)} 的分组机制，或使用显式传 SecretKey 的重载。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class Rc2Helper {
    private Rc2Helper() {
    }

    @Getter
    private static Rc2Properties properties = new Rc2Properties();

    private static void init(Rc2 rc2, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(rc2.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rc2.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rc2.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(rc2.getSignAlgorithm());
        }
    }

    /**
     * 按分组名装载 RC2 配置（实体需为 Rc2，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws Rc2Exception             配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws Rc2Exception, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc2Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rc2)) {
            throw new Rc2Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rc2 ！");
        }
        Rc2 rc2 = (Rc2) keyPair;
        if (!rc2.getEnabled()) {
            throw new Rc2Exception("Rc2 enable: false");
        }
        if (rc2.getGroupEnabled()) {
            List<KeyPair> groupList = rc2.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(rc2, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    /**
     * 装载 KeyPair 实体配置（Hex 密钥，缺省字段取默认值）
     *
     * @param keyPair 密钥对配置
     * @throws Rc2Exception 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws Rc2Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc2Exception("keyPair 不能为空！");
        }
        Rc2Properties properties = new Rc2Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = Rc2KeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithm())) {
            properties.setAlgorithm(keyPair.getAlgorithm());
        }
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithmMode())) {
            properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        }
        if (BaseUtils.isNotEmpty(keyPair.getIvParameter())) {
            properties.setIvParameter(keyPair.getIvParameter());
        }
        if (keyPair.getKeyBits() > 0) {
            properties.setKeyBits(keyPair.getKeyBits());
        }
        load(properties);
    }

    /**
     * 装载 RC2 配置（properties 与密钥不能为空；仅提供 secretKeyStr 时自动解析为密钥对象；
     * CBC 模式下 IV 必须为 8 字节）
     *
     * @param properties RC2 配置
     * @throws Rc2Exception 配置或密钥为空、密钥解析失败、算法模式不受支持、CBC 模式 IV 非法时抛出
     */
    public static void load(Rc2Properties properties) throws Rc2Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Rc2Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(Rc2KeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Rc2Exception("secretKey 不能为空！");
        }
        try {
            AlgorithmModeEnum.enumName(properties.getAlgorithmMode());
        } catch (NoSuchElementException e) {
            throw new Rc2Exception("不支持的算法模式：" + properties.getAlgorithmMode());
        }
        String algorithmMode = properties.getAlgorithmMode();
        if (algorithmMode != null && algorithmMode.contains("/CBC/")) {
            String ivParameter = properties.getIvParameter();
            if (ivParameter == null || ivParameter.getBytes(StandardCharsets.UTF_8).length != 8) {
                throw new Rc2Exception("CBC 模式下 ivParameter 不能为空且必须为 8 字节！");
            }
        }
        Rc2Helper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文；为 {@code null} 时断言失败抛 Rc2Exception
     * @return Base64 密文
     * @throws Rc2Exception 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws Rc2Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串；非法 Hex 抛 Rc2Exception
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc2Exception 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey RC2 SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc2Exception 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws Rc2Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文；非 Base64 输入抛 Rc2Exception
     * @return UTF-8 明文
     * @throws Rc2Exception 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws Rc2Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc2Exception 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey RC2 SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc2Exception 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws Rc2Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
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
