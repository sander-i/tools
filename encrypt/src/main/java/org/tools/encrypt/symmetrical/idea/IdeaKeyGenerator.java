package org.tools.encrypt.symmetrical.idea;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.aes.AesException;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 **/
@Slf4j
public class IdeaKeyGenerator {

    private IdeaKeyGenerator() {
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
     * Idea 配置文件
     */
    private static IdeaProperties properties;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws IdeaException {
        if (BaseUtils.isNull(keyGenerator)) {
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(AlgorithmEnum.IDEA.getAlgorithm());
                        keyGenerator.init(KEY_BITS);
                    }
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new IdeaException(e);
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
    public static SecretKey getSecretKey() throws IdeaException {
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
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    /**
     * 获取Idea配置文件
     *
     * @return IdeaProperties
     * @throws IdeaException IDEA加解密异常
     */
    public static IdeaProperties getProperties() throws IdeaException {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new IdeaProperties();
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
