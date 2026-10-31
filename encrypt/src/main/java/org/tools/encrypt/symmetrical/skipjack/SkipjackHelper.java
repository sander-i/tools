package org.tools.encrypt.symmetrical.skipjack;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Skipjack;
import org.tools.encrypt.entity.symmetrical.SkipjackProperties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.skipjack.SkipjackException;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.SecretKey;

/**
 * Skipjack 对称加解密工具。
 *
 * <p>Skipjack 由 BouncyCastle Provider 提供（JDK 未内置，由
 * {@link org.tools.encrypt.CipherSupply} 自动注册）。默认算法模式为 {@code SKIPJACK}
 * （ECB/PKCS5Padding，见 {@link SkipjackProperties} 初始化块），也可切换为
 * {@code SKIPJACK/CBC/PKCS5Padding}。密钥为 Hex 字符串（经
 * {@link SkipjackKeyGenerator#getSecretKey(String, String)} 解码，固定 10 字节即 80 位）；
 * CBC 模式下 IV 按 UTF-8 取字节后必须为 8 字节（Skipjack 分组长度为 64 位），
 * ECB 模式不使用 IV，传 {@code null} 即可。明文按 UTF-8 编码，密文为 Base64。</p>
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
public class SkipjackHelper {
    /**
     * Skipjack 分组长度（字节），CBC 模式的 IV 必须为该长度
     */
    private static final int BLOCK_BYTES = 8;

    private SkipjackHelper() {
    }

    @Getter
    private static SkipjackProperties properties = new SkipjackProperties();

    private static void init(Skipjack skipjack, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(skipjack.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(skipjack.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(skipjack.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(skipjack.getSignAlgorithm());
        }
    }

    /**
     * 取当前配置的初始向量：ECB 模式不使用 IV，返回 null；CBC 模式返回已配置的 IV。
     *
     * @return 初始向量
     */
    private static String ivParameter() {
        if (AlgorithmModeEnum.SKIPJACK.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            return null;
        }
        return properties.getIvParameter();
    }

    /**
     * 校验 CBC 模式的初始向量：必须非空且为 8 字节（ECB 模式不校验）。
     *
     * @param properties Skipjack 配置
     * @throws SkipjackException CBC 模式下 IV 为空或长度不符时抛出
     */
    private static void validateIv(SkipjackProperties properties) throws SkipjackException {
        if (!AlgorithmModeEnum.SKIPJACK_CBC_PKCS5PADDING.getAlgorithmMode().equals(properties.getAlgorithmMode())) {
            return;
        }
        if (BaseUtils.isEmpty(properties.getIvParameter())) {
            throw new SkipjackException("Skipjack CBC 模式下 ivParameter 不能为空！");
        }
        int ivBytes = properties.getIvParameter().getBytes(StandardCharsets.UTF_8).length;
        if (ivBytes != BLOCK_BYTES) {
            throw new SkipjackException(
                    "Skipjack CBC 模式下 ivParameter 必须为 " + BLOCK_BYTES + " 字节，实际为 " + ivBytes + " 字节！");
        }
    }

    /**
     * 按分组名装载 Skipjack 配置（实体需为 Skipjack，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws SkipjackException        配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws SkipjackException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new SkipjackException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Skipjack)) {
            throw new SkipjackException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Skipjack ！");
        }
        Skipjack skipjack = (Skipjack) keyPair;
        if (!skipjack.getEnabled()) {
            throw new SkipjackException("Skipjack enable: false");
        }
        if (skipjack.getGroupEnabled()) {
            List<KeyPair> groupList = skipjack.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(skipjack, group);
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
     * @throws SkipjackException 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws SkipjackException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new SkipjackException("keyPair 不能为空！");
        }
        SkipjackProperties properties = new SkipjackProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = SkipjackKeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
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
     * 装载 Skipjack 配置（properties 与密钥不能为空；
     * 仅提供 secretKeyStr 时自动解析为密钥对象；CBC 模式下校验 IV）
     *
     * @param properties Skipjack 配置
     * @throws SkipjackException 配置或密钥为空、密钥解析失败、算法模式不受支持、CBC 模式 IV 非法时抛出
     */
    public static void load(SkipjackProperties properties) throws SkipjackException {
        if (BaseUtils.isEmpty(properties)) {
            throw new SkipjackException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(SkipjackKeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new SkipjackException("secretKey 不能为空！");
        }
        try {
            AlgorithmModeEnum.enumName(properties.getAlgorithmMode());
        } catch (NoSuchElementException e) {
            throw new SkipjackException("不支持的算法模式：" + properties.getAlgorithmMode());
        }
        validateIv(properties);
        SkipjackHelper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文
     * @return Base64 密文
     * @throws SkipjackException 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws SkipjackException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串，解码后必须为 10 字节；非法 Hex 或长度不符抛 SkipjackException
     * @param raw       明文
     * @return Base64 密文
     * @throws SkipjackException 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws SkipjackException {
        SecretKey key = SkipjackKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey Skipjack SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws SkipjackException 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws SkipjackException {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文
     * @return UTF-8 明文
     * @throws SkipjackException 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws SkipjackException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws SkipjackException 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws SkipjackException {
        SecretKey key = SkipjackKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, ivParameter(),
                    wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey Skipjack SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws SkipjackException 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws SkipjackException {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey,
                    ivParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new SkipjackException(e);
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
