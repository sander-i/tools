package org.tools.encrypt.symmetrical.rc.rc4;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc4;
import org.tools.encrypt.entity.symmetrical.Rc4Properties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.rc.rc4.Rc4Exception;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.SecretKey;

/**
 * RC4 对称加解密工具。
 *
 * <p>RC4 为<b>流密码</b>，只有 {@code RC4} 一种算法模式：无 IV、无填充、无分组，
 * 密文长度恒等于明文长度。密钥为 Hex 字符串（经
 * {@link Rc4KeyGenerator#getSecretKey(String, String)} 解码，默认 128 位）；
 * 明文按 UTF-8 编码，密文为 Base64。调用 {@link SymmetricalUtils} 时
 * 初始向量恒传 {@code null}，配置中的 {@code ivParameter} 不参与运算。</p>
 *
 * <p>本类基于静态全局配置工作：{@code load} 替换进程内唯一的激活配置，
 * 因此 {@code load} 与 {@code encode}/{@code decode} 并发调用不安全；
 * 多次 {@code encode}/{@code decode} 之间线程安全（每次调用新建 Cipher）。
 * 多组密钥请使用 {@link #load(KeyPair, String)} 的分组机制，或使用显式传 SecretKey 的重载。</p>
 *
 * <p>安全提示：RC4 存在已知的密钥流统计偏差，不建议用于保护敏感数据。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class Rc4Helper {
    private Rc4Helper() {
    }

    @Getter
    private static Rc4Properties properties = new Rc4Properties();

    private static void init(Rc4 rc4, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(rc4.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rc4.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rc4.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(rc4.getSignAlgorithm());
        }
    }

    /**
     * 按分组名装载 RC4 配置（实体需为 Rc4，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws Rc4Exception             配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws Rc4Exception, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc4Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rc4)) {
            throw new Rc4Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rc4 ！");
        }
        Rc4 rc4 = (Rc4) keyPair;
        if (!rc4.getEnabled()) {
            throw new Rc4Exception("Rc4 enable: false");
        }
        if (rc4.getGroupEnabled()) {
            List<KeyPair> groupList = rc4.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(rc4, group);
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
     * @throws Rc4Exception 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws Rc4Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc4Exception("keyPair 不能为空！");
        }
        Rc4Properties properties = new Rc4Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = Rc4KeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithm())) {
            properties.setAlgorithm(keyPair.getAlgorithm());
        }
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithmMode())) {
            properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        }
        if (keyPair.getKeyBits() > 0) {
            properties.setKeyBits(keyPair.getKeyBits());
        }
        load(properties);
    }

    /**
     * 装载 RC4 配置（properties 与密钥不能为空；
     * 仅提供 secretKeyStr 时自动解析为密钥对象；流密码不使用 IV）
     *
     * @param properties RC4 配置
     * @throws Rc4Exception 配置或密钥为空、密钥解析失败、算法模式不受支持时抛出
     */
    public static void load(Rc4Properties properties) throws Rc4Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Rc4Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(Rc4KeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Rc4Exception("secretKey 不能为空！");
        }
        try {
            AlgorithmModeEnum.enumName(properties.getAlgorithmMode());
        } catch (NoSuchElementException e) {
            throw new Rc4Exception("不支持的算法模式：" + properties.getAlgorithmMode());
        }
        if (!AlgorithmModeEnum.RC4.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            throw new Rc4Exception("RC4 仅支持 RC4 模式！");
        }
        Rc4Helper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws Rc4Exception 密钥/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws Rc4Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), null, raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串；非法 Hex 抛 Rc4Exception
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc4Exception 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, null, raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey RC4 SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc4Exception 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws Rc4Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, null,
                    raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws Rc4Exception 密文/密钥非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws Rc4Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), null, wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc4Exception 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, null, wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey RC4 SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc4Exception 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws Rc4Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, null,
                    wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc4Exception(e);
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
