package org.tools.encrypt.symmetrical.blowfish;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.BlowfishProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.blowfish.BlowfishException;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Blowfish 密钥生成工具：生成或解析 Hex 密钥并读取配置。
 *
 * <p>Blowfish 支持 32~448 位密钥（SunJCE 实现），默认 128 位。
 * 密钥字符串为 <b>Hex</b>，与 {@link #getSecretKeyStr(SecretKey)} 输出对称。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class BlowfishKeyGenerator {
    /**
     * 默认密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 最小密钥位数
     */
    private static final int MIN_KEY_BITS = 32;

    /**
     * 最大密钥位数
     */
    private static final int MAX_KEY_BITS = 448;

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * Blowfish 配置文件
     */
    private static BlowfishProperties properties;

    private BlowfishKeyGenerator() {
    }

    /**
     * 获取默认 128 位密钥对象。
     *
     * @return SecretKey
     * @throws BlowfishException 密钥生成失败时抛出
     */
    public static SecretKey getSecretKey() throws BlowfishException {
        return getSecretKey(KEY_BITS);
    }

    /**
     * 获取指定密钥位数的密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @param keyBits 密钥位数，须为 8 的整数倍且在 32~448 之间，且不超过当前 JCE 允许的最大值
     * @return SecretKey
     * @throws BlowfishException 位数非法或密钥生成失败时抛出
     */
    public static SecretKey getSecretKey(int keyBits) throws BlowfishException {
        if (keyBits % 8 != 0) {
            throw new BlowfishException("keyBits 必须为 8 的整数倍！");
        }
        try {
            if (keyBits < MIN_KEY_BITS || keyBits > MAX_KEY_BITS) {
                throw new BlowfishException(
                        "keyBits 必须在 " + MIN_KEY_BITS + ".." + MAX_KEY_BITS + " 之间！");
            }
            int maxAllowed = Cipher.getMaxAllowedKeyLength(AlgorithmEnum.BLOWFISH.getAlgorithm());
            if (keyBits > maxAllowed) {
                throw new BlowfishException("keyBits 超过当前 JCE 允许的最大值 " + maxAllowed + "！");
            }
            KeyGenerator keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.BLOWFISH.getAlgorithm());
            keyGenerator.init(keyBits);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        } catch (InvalidParameterException e) {
            // KeyGenerator.init 对非 8 整数倍的位数抛裸 InvalidParameterException，统一包装
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
    }

    /**
     * 获取密钥字符串
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
     * <p>密钥字符串按 <b>Hex</b> 解码（与 {@link #getSecretKeyStr(SecretKey)} 输出对称，
     * 32 个 Hex 字符 = 16 字节 = 128 位）。</p>
     *
     * @param algorithm 算法
     * @param secretKey Hex 密钥字符串
     * @return 密钥对象
     * @throws BlowfishException Hex 解码失败时抛出
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws BlowfishException {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new BlowfishException("secretKey 不能为空！");
        }
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new BlowfishException(e);
        }
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 懒加载 Blowfish 配置文件（双检锁；缺密钥时自动生成）
     *
     * @return BlowfishProperties
     * @throws BlowfishException 密钥生成失败时抛出
     */
    public static BlowfishProperties getProperties() throws BlowfishException {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new BlowfishProperties();
                }
            }
        }
        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
