package org.tools.encrypt.digitalsignature.dsa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13
 * @description dsa密钥生成器
 * @scope dsa
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
     */
    public static KeyPair getKeyPair() throws DigitalSignatureException {
        init();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey
     */
    public static PublicKey getPublicKey() throws DigitalSignatureException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PublicKey
     */
    public static PrivateKey getPrivateKey() throws DigitalSignatureException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    public static String getPublicKeyStr() throws DigitalSignatureException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String
     */
    public static String getPrivateKeyStr() throws DigitalSignatureException {
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
    public static PublicKey getPublicKey(String algorithm, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
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
    public static PrivateKey getPrivateKey(String algorithm, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

}
