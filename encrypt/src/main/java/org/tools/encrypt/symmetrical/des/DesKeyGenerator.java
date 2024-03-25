package org.tools.encrypt.symmetrical.des;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.EncryptKeyGenerator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:27
 * @description des密钥生成工具
 * @scope des
 */
@Deprecated
@Slf4j
public class DesKeyGenerator {

    private DesKeyGenerator() {
    }

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
    private static DesProperties desProperties;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws DesException {
        if (BaseUtils.isNull(keyGenerator)) {
            // 线程安全
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyGenerator)) {
                        keyGenerator = EncryptKeyGenerator.getKeyGenerator(DesProperties.ALGORITHM, desProperties.getKeyBits());
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
    public static SecretKey getSecretKey(String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        DESKeySpec desKey = new DESKeySpec(secretKeyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DesProperties.ALGORITHM);
        return keyFactory.generateSecret(desKey);
    }

    /**
     * 单例模式-线程安全
     */
    private static void newInstance() {
        if (BaseUtils.isNull(desProperties)) {
            // 线程安全
            synchronized (LOCK) {
                if (BaseUtils.isNull(desProperties)) {
                    desProperties = new DesProperties();
                }
            }
        }
    }

    /**
     * 获取Des配置文件
     *
     * @return DesProperties
     */
    public static DesProperties getDesProperties() throws DesException {
        newInstance();
        if (BaseUtils.isNull(desProperties.getSecretKey())) {
            SecretKey secretKey = getSecretKey();
            String secretKeyStr = getSecretKeyStr(secretKey);
            desProperties.setSecretKey(secretKeyStr);
        }
        return desProperties;
    }

}
