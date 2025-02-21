package org.tools.encrypt.digitalsignature.dsa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class DsaKeyGenerator {

    private DsaKeyGenerator() {
    }

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    @Getter
    @Setter
    private static String algorithm = AlgorithmEnum.DSA.getAlgorithm();

    @Getter
    @Setter
    private static int keyBits = 1024;

    /**
     * Dsa 配置文件
     */
    private static DsaProperties properties;

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
    private static void init() throws DigitalSignatureException {
        if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
            // 线程安全
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
                        keyPairGenerator = EncryptKeyGenerator.getKeyPairGenerator(algorithm);
                        keyPairGenerator.initialize(keyBits);
                    }
                    keyPair = keyPairGenerator.generateKeyPair();
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new DigitalSignatureException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     * @throws DigitalSignatureException 签名异常
     */
    public static KeyPair getKeyPair() throws DigitalSignatureException {
        init();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey
     * @throws DigitalSignatureException 签名异常
     */
    public static PublicKey getPublicKey() throws DigitalSignatureException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PublicKey
     * @throws DigitalSignatureException 签名异常
     */
    public static PrivateKey getPrivateKey() throws DigitalSignatureException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return String
     * @throws DigitalSignatureException 签名异常
     */
    public static String getPublicKeyStr() throws DigitalSignatureException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return 私钥字符串
     * @throws DigitalSignatureException 签名异常
     */
    public static String getPrivateKeyStr() throws DigitalSignatureException {
        return Base64.encodeBase64String(getKeyPair().getPrivate().getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥对象
     * @return 公钥对象
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥对象
     * @return 私钥字符串
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param algorithm 算法
     * @param publicKey 公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PublicKey getPublicKey(String algorithm, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param algorithm  算法
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PrivateKey getPrivateKey(String algorithm, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
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
                    properties = new DsaProperties();
                }
            }
        }
    }

    /**
     * 获取Dsa配置文件
     *
     * @return DsaProperties
     * @throws EncryptException 加解密异常
     */
    public static DsaProperties getDsaProperties() throws EncryptException {
        initProperties();

        if (BaseUtils.isEmpty(properties.getPublicKeyStr(), properties.getPrivateKeyStr()) ||
                BaseUtils.isEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
            properties.setPublicKey(RsaKeyGenerator.getPublicKey());
            properties.setPrivateKey(RsaKeyGenerator.getPrivateKey());
            properties.setPublicKeyStr(RsaKeyGenerator.getPublicKeyStr(properties.getPublicKey()));
            properties.setPrivateKeyStr(RsaKeyGenerator.getPrivateKeyStr(properties.getPrivateKey()));
        }
        return properties;
    }

}
