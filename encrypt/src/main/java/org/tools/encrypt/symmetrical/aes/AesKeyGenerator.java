package org.tools.encrypt.symmetrical.aes;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;

/**
 * AES密钥生成工具
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.1
 */
@Slf4j
public class AesKeyGenerator {
    /**
     * 密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 锁对象（properties 懒加载）
     */
    private static final Object LOCK = new Object();

    /**
     * Aes 配置文件
     */
    private static AesProperties properties;

    private AesKeyGenerator() {
    }

    /**
     * 获取密钥对象。
     *
     * <p>每次调用创建独立的 KeyGenerator（JCA 生成器非线程安全，
     * 共享实例并发 {@code init}/{@code generateKey} 会互相污染）。</p>
     *
     * @return SecretKey
     * @throws AesException AES加解密异常
     */
    public static SecretKey getSecretKey() throws AesException {
        try {
            KeyGenerator keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.AES.getAlgorithm());
            keyGenerator.init(KEY_BITS);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new AesException(e);
        }
    }

    /**
     * 获取密钥字符串
     *
     * @param secretKey 密钥对象
     * @return 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) {
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Hex.decodeHex(secretKey.toCharArray());
        } catch (DecoderException e) {
            log.error(e.getLocalizedMessage());
            throw new AesException(e);
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
                    properties = new AesProperties();
                }
            }
        }
    }

    /**
     * 获取Aes配置文件
     *
     * @return AesProperties
     * @throws AesException AES加解密异常
     */
    public static AesProperties getProperties() throws AesException {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }
}
