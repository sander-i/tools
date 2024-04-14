package org.tools.encrypt.symmetrical.des;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.exception.symmetrical.des.DesException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03
 * @description des密钥生成工具
 * @scope des
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
     */
    public static SecretKey getSecretKey() throws DesException {
        init();
        return keyGenerator.generateKey();
    }

    /**
     * 获取密钥字符串
     *
     * @return String
     */
    public static String getSecretKeyStr(SecretKey secretKey) {
        return Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String algorithm, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretKeyBytes, algorithm);
    }

    public static DesProperties getProperties() throws DesException {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new DesProperties();
                }
            }
        }
        if (BaseUtils.isEmpty(properties.getSecretKey(), properties.getDesKey())) {
            properties.setDesKey(getSecretKey());
            properties.setSecretKey(getSecretKeyStr(properties.getDesKey()));
        }
        return properties;
    }

}
