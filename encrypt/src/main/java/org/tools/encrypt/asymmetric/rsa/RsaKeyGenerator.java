package org.tools.encrypt.asymmetric.rsa;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author sander
 * @version 1.0
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
     *
     * @throws RsaException RSA加解密异常
     */
    private static void init() throws RsaException {
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
                    throw new RsaException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     * @throws RsaException RSA加解密异常
     */
    public static KeyPair getKeyPair() throws RsaException {
        init();

        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey
     * @throws RsaException RSA加解密异常
     */
    public static PublicKey getPublicKey() throws RsaException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PublicKey
     * @throws RsaException RSA加解密异常
     */
    public static PrivateKey getPrivateKey() throws RsaException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return 公钥字符串
     * @throws EncryptException 加解密异常
     */
    public static String getPublicKeyStr() throws EncryptException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String
     * @throws EncryptException 加解密异常
     */
    public static String getPrivateKeyStr() throws EncryptException {
        return Base64.encodeBase64String(getKeyPair().getPrivate().getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥对象
     * @return 公钥字符串
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥对象
     * @return 私钥对象
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
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
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
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
     * @throws EncryptException 加解密异常
     */
    public static RsaProperties getRsaProperties() throws EncryptException {
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
