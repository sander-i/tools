package org.tools.encrypt.symmetrical.rc.rc2;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.symmetrical.Rc2Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc2.Rc2Exception;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * RC2 密钥生成工具：生成随机密钥、解析 Hex 密钥字符串并读取配置。
 *
 * <p>JCA 未提供 {@code KeyGenerator} 形式的 RC2 生成器，因此随机密钥由 {@link SecureRandom}
 * 生成指定位数的字节后包装为 {@link SecretKeySpec}。密钥位数允许 40..1024，
 * 且不得超过 JCE 允许的最大值（见 {@link Cipher#getMaxAllowedKeyLength(String)}）。
 * SunJCE 对 RC2 密钥的合法范围为 40~1024 位，低于 40 位会抛
 * {@code InvalidKeyException: RC2 key length must be between 40 and 1024 bit}。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class Rc2KeyGenerator {
    /**
     * 默认密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 允许的最小密钥位数（SunJCE RC2 下限 40 位）
     */
    private static final int MIN_KEY_BITS = 40;

    /**
     * 允许的最大密钥位数
     */
    private static final int MAX_KEY_BITS = 1024;

    /**
     * 允许的最小密钥字节数（40 位 = 5 字节）
     */
    private static final int MIN_KEY_BYTES = 5;

    /**
     * 允许的最大密钥字节数（1024 位 = 128 字节）
     */
    private static final int MAX_KEY_BYTES = 128;

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * 随机源（SecureRandom 线程安全）
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Rc2 配置文件
     */
    private static Rc2Properties properties;

    private Rc2KeyGenerator() {
    }

    /**
     * 获取默认位数（128 位）的随机密钥。
     *
     * @return SecretKey
     * @throws Rc2Exception 算法不可用或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey() throws Rc2Exception {
        return getSecretKey(KEY_BITS);
    }

    /**
     * 获取指定位数的随机密钥。
     *
     * @param keyBits 密钥位数，必须在 40..1024 之间且不超过 JCE 允许的最大值
     * @return SecretKey（算法名为 RC2）
     * @throws Rc2Exception 位数越界、算法不可用或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey(int keyBits) throws Rc2Exception {
        if (keyBits < MIN_KEY_BITS || keyBits > MAX_KEY_BITS) {
            throw new Rc2Exception("keyBits 必须在 " + MIN_KEY_BITS + ".." + MAX_KEY_BITS + " 之间！");
        }
        int maxAllowedKeyLength;
        try {
            maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC2.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
        if (keyBits > maxAllowedKeyLength) {
            throw new Rc2Exception("keyBits 不能超过 JCE 允许的最大值 " + maxAllowedKeyLength + "！");
        }
        byte[] secretKeyBytes = new byte[(keyBits + 7) / 8];
        RANDOM.nextBytes(secretKeyBytes);
        return new SecretKeySpec(secretKeyBytes, AlgorithmEnum.RC2.getAlgorithm());
    }

    /**
     * 获取密钥字符串（Hex）。
     *
     * @param secretKey 密钥对象
     * @return Hex 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象。
     *
     * <p>密钥字符串为 <b>Hex</b>（与 {@link #getSecretKeyStr(SecretKey)} 输出对称，
     * 32 个 Hex 字符 = 16 字节 = 128 位）。</p>
     *
     * @param algorithm 算法
     * @param secretKey Hex 密钥字符串
     * @return 密钥对象
     * @throws Rc2Exception 密钥字符串不是合法 Hex 或解码后长度不在 5..128 字节时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws Rc2Exception {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new Rc2Exception("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new Rc2Exception(e);
        }
        // 解析阶段即拒绝越界长度，避免把非法密钥推迟到 Cipher.init 才暴露
        if (secretKeyBytes.length < MIN_KEY_BYTES || secretKeyBytes.length > MAX_KEY_BYTES) {
            throw new Rc2Exception("secretKey 解码后长度必须在 " + MIN_KEY_BYTES + ".." + MAX_KEY_BYTES
                    + " 字节之间！");
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 单例模式-线程安全
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new Rc2Properties();
                }
            }
        }
    }

    /**
     * 获取 Rc2 配置文件（首次调用时生成默认密钥）
     *
     * @return Rc2Properties
     * @throws Rc2Exception 密钥生成失败时抛出
     */
    public static Rc2Properties getProperties() throws Rc2Exception {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
