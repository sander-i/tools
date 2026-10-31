package org.tools.encrypt.symmetrical.blowfish;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Blowfish;
import org.tools.encrypt.entity.symmetrical.BlowfishProperties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.blowfish.BlowfishException;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.SecretKey;

/**
 * Blowfish 对称加解密工具。
 *
 * <p>默认算法模式为 {@code Blowfish}（ECB/PKCS5Padding，见 {@link BlowfishProperties} 初始化块），
 * 也可切换为 {@code Blowfish/CBC/PKCS5Padding}。密钥为 Hex 字符串（经
 * {@link BlowfishKeyGenerator#getSecretKey(String, String)} 解码，长度 4~56 字节）；CBC 模式下
 * IV 按 UTF-8 取字节后必须为 8 字节（Blowfish 分组长度为 64 位），ECB 模式不使用 IV，
 * 传 {@code null} 即可。明文按 UTF-8 编码，密文为 Base64。</p>
 *
 * <p>本类基于静态全局配置工作：{@code load} 替换进程内唯一的激活配置，
 * 因此 {@code load} 与 {@code encode}/{@code decode} 并发调用不安全；
 * 多次 {@code encode}/{@code decode} 之间线程安全（每次调用新建 Cipher）。
 * 多组密钥请使用 {@link #load(KeyPair, String)} 的分组机制，或使用显式传 SecretKey 的重载。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class BlowfishHelper {
    /**
     * CBC 模式初始向量字节数（Blowfish 分组长度 64 位）
     */
    private static final int IV_BYTES = 8;

    /**
     * CBC 模式标识
     */
    private static final String CBC_MARK = "/CBC/";

    private BlowfishHelper() {
    }

    @Getter
    private static BlowfishProperties properties = new BlowfishProperties();

    private static void init(Blowfish blowfish, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(blowfish.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(blowfish.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(blowfish.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(blowfish.getSignAlgorithm());
        }
    }

    /**
     * 取当前配置的初始向量：ECB 模式不使用 IV，返回 null；CBC 模式返回已配置的 IV。
     *
     * @return 初始向量
     */
    private static String ivParameter() {
        if (AlgorithmModeEnum.BLOWFISH.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            return null;
        }
        return properties.getIvParameter();
    }

    /**
     * 按分组名装载 Blowfish 配置（实体需为 Blowfish，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws BlowfishException        配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws BlowfishException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new BlowfishException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Blowfish)) {
            throw new BlowfishException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Blowfish ！");
        }
        Blowfish blowfish = (Blowfish) keyPair;
        if (!blowfish.getEnabled()) {
            throw new BlowfishException("Blowfish enable: false");
        }
        if (blowfish.getGroupEnabled()) {
            List<KeyPair> groupList = blowfish.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(blowfish, group);
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
     * @throws BlowfishException 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws BlowfishException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new BlowfishException("keyPair 不能为空！");
        }
        BlowfishProperties properties = new BlowfishProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = BlowfishKeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
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
     * 装载 Blowfish 配置（properties 与密钥不能为空；
     * 仅提供 secretKeyStr 时自动解析为密钥对象；CBC 模式要求 IV 为 8 字节）
     *
     * @param properties Blowfish 配置
     * @throws BlowfishException 配置或密钥为空、密钥解析失败、算法模式不受支持、CBC 模式 IV 非法时抛出
     */
    public static void load(BlowfishProperties properties) throws BlowfishException {
        if (BaseUtils.isEmpty(properties)) {
            throw new BlowfishException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(BlowfishKeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new BlowfishException("secretKey 不能为空！");
        }
        try {
            AlgorithmModeEnum.enumName(properties.getAlgorithmMode());
        } catch (NoSuchElementException e) {
            throw new BlowfishException("不支持的算法模式：" + properties.getAlgorithmMode());
        }
        String algorithmMode = properties.getAlgorithmMode();
        if (algorithmMode != null && algorithmMode.contains(CBC_MARK)) {
            String ivParameter = properties.getIvParameter();
            if (BaseUtils.isEmpty(ivParameter) || ivParameter.getBytes(StandardCharsets.UTF_8).length != IV_BYTES) {
                throw new BlowfishException("CBC 模式 ivParameter 不能为空且必须为 8 字节！");
            }
        }
        BlowfishHelper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws BlowfishException 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws BlowfishException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串，长度 4~56 字节；非法 Hex 抛 BlowfishException
     * @param raw       明文
     * @return Base64 密文
     * @throws BlowfishException 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey Blowfish SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws BlowfishException 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws BlowfishException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws BlowfishException 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws BlowfishException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws BlowfishException 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey Blowfish SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws BlowfishException 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws BlowfishException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
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
