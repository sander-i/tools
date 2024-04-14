package org.tools.encrypt.asymmetric.rsa;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.exception.EncryptException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11
 * @description rsa密钥生成器
 * @scope rsa
 */
@Slf4j
public class RsaKeyGenerator {

    private RsaKeyGenerator() {
    }

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * Rsa 配置文件
     */
    @Setter
    private static RsaProperties properties;

    /**
     * 密钥对生成器
     */
    private static KeyPairGenerator keyPairGenerator;

    /**
     * 密钥对
     */
    private static KeyPair keyPair;

    /**
     * 初始化密钥生成器对象
     */
    private static void init() throws EncryptException {
        if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
            // 线程安全
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
                        keyPairGenerator = EncryptKeyGenerator.getKeyPairGenerator(properties.getAlgorithm());
                        keyPairGenerator.initialize(properties.getKeyBits());
                    }
                    keyPair = keyPairGenerator.generateKeyPair();
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new EncryptException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     */
    public static KeyPair getKeyPair() throws EncryptException {
        init();

        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey
     */
    public static PublicKey getPublicKey() throws EncryptException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PublicKey
     */
    public static PrivateKey getPrivateKey() throws EncryptException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    public static String getPublicKeyStr() throws EncryptException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String
     */
    public static String getPrivateKeyStr() throws EncryptException {
        return Base64.encodeBase64String(getKeyPair().getPrivate().getEncoded());
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        initProperties();

        KeyFactory keyFactory = KeyFactory.getInstance(properties.getAlgorithm());
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        initProperties();

        KeyFactory keyFactory = KeyFactory.getInstance(properties.getAlgorithm());
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 单例模式-线程安全
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            // 线程安全
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new RsaProperties();
                }
            }
        }
    }

    /**
     * 获取Rsa配置文件
     *
     * @return RsaProperties
     */
    public static RsaProperties getRsaProperties() throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException {
        initProperties();

        if (BaseUtils.isEmpty(properties.getPublicKey(), properties.getPrivateKey()) ||
                BaseUtils.isEmpty(properties.getRsaPublicKey(), properties.getRsaPrivateKey())) {
            properties.setRsaPublicKey(RsaKeyGenerator.getPublicKey());
            properties.setRsaPrivateKey(RsaKeyGenerator.getPrivateKey());
            properties.setPublicKey(RsaKeyGenerator.getPublicKeyStr(properties.getRsaPublicKey()));
            properties.setPrivateKey(RsaKeyGenerator.getPrivateKeyStr(properties.getRsaPrivateKey()));
        }
        return properties;
    }

}
