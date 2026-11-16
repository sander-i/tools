package io.github.sander_i.encrypt.symmetrical.aes;

import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.EncryptKeyGenerator;
import io.github.sander_i.encrypt.entity.symmetrical.AesProperties;
import io.github.sander_i.encrypt.SecretKeyCodec;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * des密钥生成工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class AesKeyGenerator {

    private AesKeyGenerator() {
    }

    /**
     * 密钥位数
     */
    private static final int KEY_BITS = 128;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * 密钥生成器
     */
    private static KeyGenerator keyGenerator;

    /**
     * Aes 配置文件
     */
    private static AesProperties properties;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws AesException {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.AES.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new AesException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     * @throws AesException AES加解密异常
     */
    public static SecretKey getSecretKey() throws AesException {
        init();
        return keyGenerator.generateKey();
    }

    /**
     * 获取密钥字符串
     *
     * @param secretKey 密钥对象
     * @return 密钥字符串
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return SecretKeyCodec.encode(secretKey);
    }

    /**
     * 获取密钥对象
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws EncryptException {
        return SecretKeyCodec.decode(algorithm, secretKey);
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
