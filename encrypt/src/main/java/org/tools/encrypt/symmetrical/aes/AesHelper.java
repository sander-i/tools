package org.tools.encrypt.symmetrical.aes;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.exception.symmetrical.aes.AesException;
import org.tools.encrypt.symmetrical.SymmetricalUtils;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * AES 对称加解密工具。
 *
 * <p>默认算法模式为 {@code AES/CBC/PKCS5Padding}（见 {@link AesProperties} 初始化块）。
 * 密钥为 Hex 字符串（16/24/32 字节对应 AES-128/192/256，经 {@link AesKeyGenerator#getSecretKey(String, String)}
 * 解码）；CBC 模式下 IV 按 UTF-8 取字节后必须为 16 字节。明文按 UTF-8 编码，密文为 Base64。</p>
 *
 * <p>本类基于静态全局配置工作：{@code load} 替换进程内唯一的激活配置，
 * 因此 {@code load} 与 {@code encode}/{@code decode} 并发调用不安全；
 * 多次 {@code encode}/{@code decode} 之间线程安全（每次调用新建 Cipher）。
 * 多组密钥请使用 {@link #load(KeyPair, String)} 的分组机制，或使用显式传 SecretKey 的重载。</p>
 *
 * @author sander 2024/04/05
 */
public class AesHelper {

    private AesHelper() {
    }

    @Getter
    private static AesProperties properties = new AesProperties();

    private static void init(Aes aes, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(aes.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(aes.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(aes.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(aes.getSignAlgorithm());
        }
    }

    public static void load(KeyPair keyPair, String name) throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new AesException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Aes)) {
            throw new AesException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Aes ！");
        }
        Aes aes = (Aes) keyPair;
        if (!aes.getEnabled()) {
            throw new AesException("Aes enable: false");
        }
        if (aes.getGroupEnabled()) {
            List<KeyPair> groupList = aes.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(aes, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    public static void load(KeyPair keyPair) throws AesException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new AesException("keyPair 不能为空！");
        }
        AesProperties properties = new AesProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        SecretKey secretKey = AesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    public static void load(AesProperties properties) throws AesException {
        if (BaseUtils.isEmpty(properties)) {
            throw new AesException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new AesException("secretKey 不能为空！");
        }
        AesHelper.properties = properties;
    }

    /**
     * 使用全局激活配置加密。
     *
     * @param raw 明文；为 {@code null} 时断言失败抛 AesException
     * @return Base64 密文
     * @throws AesException 密钥/IV/模式非法或 JCE 操作失败时抛出（异常链保留）
     */
    public static String encode(String raw) throws AesException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), properties.getIvParameter(), raw);
    }

    /**
     * 使用指定密钥加密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串，解码后须为 16/24/32 字节；非法 Hex 抛 AesException
     * @param raw       明文
     * @return Base64 密文
     * @throws AesException 同 {@link #encode(String)}
     */
    public static String encode(String secretKey, String raw) throws AesException {
        SecretKey key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    /**
     * 使用指定密钥对象加密（其余参数取全局激活配置）。
     *
     * @param secretKey AES SecretKey
     * @param raw       明文
     * @return Base64 密文
     * @throws AesException 同 {@link #encode(String)}
     */
    public static String encode(SecretKey secretKey, String raw) throws AesException {
        return SymmetricalUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    /**
     * 使用全局激活配置解密。
     *
     * @param wrought Base64 密文；非 Base64 输入抛 AesException
     * @return UTF-8 明文
     * @throws AesException 密文/密钥/IV 非法或 JCE 操作失败时抛出
     */
    public static String decode(String wrought) throws AesException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), properties.getIvParameter(), wrought);
    }

    /**
     * 使用指定密钥解密（其余参数取全局激活配置）。
     *
     * @param secretKey Hex 密钥字符串
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws AesException 同 {@link #decode(String)}
     */
    public static String decode(String secretKey, String wrought) throws AesException {
        SecretKey key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    /**
     * 使用指定密钥对象解密（其余参数取全局激活配置）。
     *
     * @param secretKey AES SecretKey
     * @param wrought   Base64 密文
     * @return UTF-8 明文
     * @throws AesException 同 {@link #decode(String)}
     */
    public static String decode(SecretKey secretKey, String wrought) throws AesException {
        return SymmetricalUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
