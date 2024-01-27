package org.tools.encrypt.digitalsignature.dsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.exception.digitalsignature.dsa.DsaException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:23
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
    /**
     * Dsa 配置文件
     */
    private static DsaProperties dsaProperties;
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
    private static void init() throws DsaException {
        if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
            // 线程安全
            synchronized (LOCK) {
                try {
                    if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
                        keyPairGenerator = EncryptKeyGenerator.getKeyPairGenerator(DsaProperties.ALGORITHM, dsaProperties.getKeyBits());
                    }
                    keyPair = keyPairGenerator.generateKeyPair();
                } catch (NoSuchAlgorithmException e) {
                    log.error(e.getLocalizedMessage());
                    throw new DsaException(e);
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return SecretKey
     */
    public static KeyPair getKeyPair() throws DsaException {
        init();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey
     */
    public static PublicKey getPublicKey() throws DsaException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PublicKey
     */
    public static PrivateKey getPrivateKey() throws DsaException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    public static String getPublicKeyStr() throws DsaException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String
     */
    public static String getPrivateKeyStr() throws DsaException {
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
        KeyFactory keyFactory = KeyFactory.getInstance(DsaProperties.ALGORITHM);
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
        KeyFactory keyFactory = KeyFactory.getInstance(DsaProperties.ALGORITHM);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 单例模式-线程安全
     */
    private static void newInstance() {
        if (BaseUtils.isNull(dsaProperties)) {
            // 线程安全
            synchronized (LOCK) {
                if (BaseUtils.isNull(dsaProperties)) {
                    dsaProperties = new DsaProperties();
                }
            }
        }
    }

    /**
     * 获取Dsa配置文件
     *
     * @return DsaProperties
     */
    public static DsaProperties getDsaProperties() throws DsaException {
        newInstance();
        if (BaseUtils.isNull(dsaProperties.getPublicKey(), dsaProperties.getPrivateKey())) {
            dsaProperties.setPublicKey(getPublicKeyStr());
            dsaProperties.setPrivateKey(getPrivateKeyStr());
        }
        return dsaProperties;
    }

}
