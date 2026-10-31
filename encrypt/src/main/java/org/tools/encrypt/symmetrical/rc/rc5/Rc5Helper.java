package org.tools.encrypt.symmetrical.rc.rc5;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Rc5;
import org.tools.encrypt.entity.symmetrical.Rc5Properties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.rc.rc5.Rc5Exception;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.SecretKey;

/**
 * RC5 对称加解密工具。
 *
 * <p>默认算法模式为 {@code RC5}（ECB/PKCS5Padding，见 {@link Rc5Properties} 初始化块），
 * 也可切换为 {@code RC5/CBC/PKCS5Padding}。RC5 由 BouncyCastle Provider 提供，
 * 注册与 {@code RC5ParameterSpec} 的构造由 {@link org.tools.encrypt.CipherSupply} 负责，
 * 参数固定为 {@link #ROUNDS} 轮、{@link #BLOCK_SIZE} 字节分组、{@link #WORD_SIZE} 位字长，
 * 调用方需使用相同参数才能互通。密钥为 Hex 字符串（经
 * {@link Rc5KeyGenerator#getSecretKey(String, String)} 解码，默认 16 字节即 128 位）；
 * CBC 模式下 IV 按 UTF-8 取字节后必须为 8 字节（RC5 分组长度 64 位），ECB 模式不使用 IV，
 * 传 {@code null} 即可。明文按 UTF-8 编码，密文为 Base64。</p>
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
public class Rc5Helper {
    /**
     * RC5 轮数（BC 默认参数，需与 CipherSupply 保持一致）
     */
    public static final int ROUNDS = 12;

    /**
     * RC5 分组长度（字节，64 位）
     */
    public static final int BLOCK_SIZE = 8;

    /**
     * RC5 字长（位，32 位）
     */
    public static final int WORD_SIZE = 32;

    private Rc5Helper() {
    }

    @Getter
    private static Rc5Properties properties = new Rc5Properties();

    private static void init(Rc5 rc5, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(rc5.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rc5.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rc5.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(rc5.getSignAlgorithm());
        }
    }

    /**
     * 取当前配置的初始向量：ECB 模式不使用 IV，返回 null；CBC 模式返回已配置的 IV。
     *
     * @return 初始向量
     */
    private static String ivParameter() {
        if (AlgorithmModeEnum.RC5.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            return null;
        }
        return properties.getIvParameter();
    }

    /**
     * 按分组名装载 RC5 配置（实体需为 Rc5，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws Rc5Exception             配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws Rc5Exception, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc5Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rc5)) {
            throw new Rc5Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rc5 ！");
        }
        Rc5 rc5 = (Rc5) keyPair;
        if (!rc5.getEnabled()) {
            throw new Rc5Exception("Rc5 enable: false");
        }
        if (rc5.getGroupEnabled()) {
            List<KeyPair> groupList = rc5.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(rc5, group);
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
     * @throws Rc5Exception 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws Rc5Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Rc5Exception("keyPair 不能为空！");
        }
        Rc5Properties properties = new Rc5Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = Rc5KeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
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
     * 装载 RC5 配置（properties 与密钥不能为空；仅提供 secretKeyStr 时自动解析为密钥对象；
     * CBC 模式下 IV 必须为 8 字节）
     *
     * @param properties RC5 配置
     * @throws Rc5Exception 配置或密钥为空、密钥解析失败、算法模式不受支持、CBC 模式 IV 非法时抛出
     */
    public static void load(Rc5Properties properties) throws Rc5Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Rc5Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(Rc5KeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Rc5Exception("secretKey 不能为空！");
        }
        try {
            AlgorithmModeEnum.enumName(properties.getAlgorithmMode());
        } catch (NoSuchElementException e) {
            throw new Rc5Exception("不支持的算法模式：" + properties.getAlgorithmMode());
        }
        if (AlgorithmModeEnum.RC5_CBC_PKCS5PADDING.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            String ivParameter = properties.getIvParameter();
            if (ivParameter == null || ivParameter.getBytes(StandardCharsets.UTF_8).length != BLOCK_SIZE) {
                throw new Rc5Exception("CBC 模式下 ivParameter 不能为空且必须为 " + BLOCK_SIZE + " 字节！");
            }
        }
        Rc5Helper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws Rc5Exception 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws Rc5Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串；非法 Hex 抛 Rc5Exception
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc5Exception 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws Rc5Exception {
        SecretKey key = Rc5KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey RC5 SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws Rc5Exception 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws Rc5Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws Rc5Exception 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws Rc5Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc5Exception 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws Rc5Exception {
        SecretKey key = Rc5KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey RC5 SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Rc5Exception 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws Rc5Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc5Exception(e);
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
