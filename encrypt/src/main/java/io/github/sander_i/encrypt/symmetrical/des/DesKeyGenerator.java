package io.github.sander_i.encrypt.symmetrical.des;

import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.EncryptKeyGenerator;
import io.github.sander_i.encrypt.entity.symmetrical.DesProperties;
import io.github.sander_i.encrypt.SecretKeyCodec;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.EncryptException;
import io.github.sander_i.encrypt.exception.symmetrical.des.DesException;

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
public class DesKeyGenerator {

    private DesKeyGenerator() {
    }

    /**
     * 密钥位数
     */
    private static final int KEY_BITS = 56;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();
    /**
     * 密钥生成器
     */
    private static KeyGenerator keyGenerator;

    /**
     * Des 配置文件
     */
    private static DesProperties properties;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws DesException {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.DES.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new DesException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     * @throws DesException DES加解密异常
     */
    public static SecretKey getSecretKey() throws DesException {
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
                    properties = new DesProperties();
                }
            }
        }
    }

    /**
     * 获取Des配置文件
     *
     * @return DesProperties
     * @throws DesException DES加解密异常
     */
    public static DesProperties getProperties() throws DesException {
        initProperties();

        if (BaseUtils.isEmpty(properties.getSecretKeyStr(), properties.getSecretKey())) {
            properties.setSecretKey(getSecretKey());
            properties.setSecretKeyStr(getSecretKeyStr(properties.getSecretKey()));
        }
        return properties;
    }

}
