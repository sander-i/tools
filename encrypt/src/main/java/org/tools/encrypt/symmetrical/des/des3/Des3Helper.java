package org.tools.encrypt.symmetrical.des.des3;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Des3;
import org.tools.encrypt.entity.symmetrical.Des3Properties;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;
import org.tools.encrypt.exception.symmetrical.des3.Des3Exception;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.SecretKey;

/**
 * 3DES（DESede，等同 TDEA / Triple DES）对称加解密工具。
 *
 * <p>默认算法模式为 {@code DESede/CBC/PKCS5Padding}（见 {@link Des3Properties} 初始化块）。
 * 密钥为 Hex 字符串（16 字节 / 32 Hex 或 24 字节 / 48 Hex，经
 * {@link Des3KeyGenerator#getSecretKey(String, String)} 解码）；CBC 模式下 IV 按 UTF-8
 * 取字节后必须为 8 字节。明文按 UTF-8 编码，密文为 Base64。</p>
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
public class Des3Helper {
    /**
     * CBC 模式初始向量字节数
     */
    private static final int IV_BYTES = 8;

    /**
     * 3DES 唯一受支持的算法模式
     */
    private static final String SUPPORTED_ALGORITHM_MODE = AlgorithmModeEnum.DES_EDE_CBC_PKCS5Padding
            .getAlgorithmMode();

    private Des3Helper() {
    }

    @Getter
    private static Des3Properties properties = new Des3Properties();

    private static void init(Des3 des3, KeyPair keyPair) {
        if (keyPair.getKeyBits() <= 0) {
            keyPair.setKeyBits(des3.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(des3.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(des3.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(des3.getSignAlgorithm());
        }
    }

    /**
     * 按分组名装载 3DES 配置（实体需为 Des3，缺省字段由顶层配置补齐）
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws Des3Exception            配置非法或分组不存在时抛出
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     * @throws InvalidKeySpecException  密钥规格非法时抛出
     * @throws InvalidKeyException      密钥非法时抛出
     */
    public static void load(KeyPair keyPair, String name) throws Des3Exception, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Des3Exception("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Des3)) {
            throw new Des3Exception("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Des3 ！");
        }
        Des3 des3 = (Des3) keyPair;
        if (!des3.getEnabled()) {
            throw new Des3Exception("Des3 enable: false");
        }
        if (des3.getGroupEnabled()) {
            List<KeyPair> groupList = des3.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(des3, group);
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
     * @throws Des3Exception 配置为空或密钥非法时抛出
     */
    public static void load(KeyPair keyPair) throws Des3Exception {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new Des3Exception("keyPair 不能为空！");
        }
        Des3Properties properties = new Des3Properties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : properties.getAlgorithm();
        SecretKey secretKey = Des3KeyGenerator.getSecretKey(algorithm, keyPair.getSecretKey());
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
     * 装载 3DES 配置（properties 与密钥不能为空；仅提供 secretKeyStr 时自动解析为密钥对象；
     * 仅支持 {@code DESede/CBC/PKCS5Padding} 模式且 IV 必须为 8 字节）
     *
     * @param properties 3DES 配置
     * @throws Des3Exception 配置为空、密钥为空/解析失败、模式不受支持或 CBC 模式 IV 非法时抛出
     */
    public static void load(Des3Properties properties) throws Des3Exception {
        if (BaseUtils.isEmpty(properties)) {
            throw new Des3Exception("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey()) && !BaseUtils.isEmpty(properties.getSecretKeyStr())) {
            properties.setSecretKey(Des3KeyGenerator.getSecretKey(properties.getAlgorithm(),
                    properties.getSecretKeyStr()));
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new Des3Exception("secretKey 不能为空！");
        }
        if (!SUPPORTED_ALGORITHM_MODE.equals(properties.getAlgorithmMode())) {
            throw new Des3Exception("3DES 仅支持 DESede/CBC/PKCS5Padding 模式！");
        }
        String ivParameter = properties.getIvParameter();
        if (BaseUtils.isEmpty(ivParameter) || ivParameter.getBytes(StandardCharsets.UTF_8).length != IV_BYTES) {
            throw new Des3Exception("CBC 模式 ivParameter 不能为空且按 UTF-8 编码必须为 8 字节！");
        }
        Des3Helper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文；为 {@code null} 时参数校验失败
     * @return Base64 密文
     * @throws Des3Exception 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws Des3Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串，解码后须为 16/24 字节；非法 Hex 抛 Des3Exception
     * @param raw       明文
     * @return Base64 密文
     * @throws Des3Exception 密钥非法或加密失败时抛出
     */
    public static String encode(String secretKey, String raw) throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey DESede SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws Des3Exception 密钥非法或加密失败时抛出
     */
    public static String encode(SecretKey secretKey, String raw) throws Des3Exception {
        try {
            return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), raw);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文；非 Base64 输入抛运行时异常
     * @return UTF-8 明文
     * @throws Des3Exception 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws Des3Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties
                    .getSecretKey(), properties.getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Des3Exception 密钥非法或解密失败时抛出
     */
    public static String decode(String secretKey, String wrought) throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
        }
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey DESede SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws Des3Exception 密钥非法或解密失败时抛出
     */
    public static String decode(SecretKey secretKey, String wrought) throws Des3Exception {
        try {
            return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties
                    .getIvParameter(), wrought);
        } catch (SymmetricalException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(unwrap(e));
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new Des3Exception(e);
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
