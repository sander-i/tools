package io.github.sander_i.encrypt.asymmetric.dh;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.exception.asymmetric.dh.DhException;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * DH（Diffie-Hellman）密钥交换工具
 *
 * <p>
 * DH 不直接加密数据，而是双方各自生成密钥对，
 * 交换公钥后协商出相同的共享密钥，再用共享密钥调用对称加密处理数据。
 * </p>
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class DhHelper {

    private DhHelper() {
    }

    /**
     * 默认素数位数
     */
    public static final int DEFAULT_KEY_BITS = 1024;

    /**
     * 生成 DH 密钥对
     *
     * @return KeyPair
     * @throws DhException DH 异常
     */
    public static KeyPair generateKeyPair() throws DhException {
        return generateKeyPair(DEFAULT_KEY_BITS);
    }

    /**
     * 按指定素数位数生成 DH 密钥对
     *
     * @param keyBits 素数位数
     * @return KeyPair
     * @throws DhException DH 异常
     */
    public static KeyPair generateKeyPair(int keyBits) throws DhException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("DH");
            generator.initialize(keyBits);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 基于已有公钥的参数生成对应的密钥对（应答方使用）
     *
     * @param publicKey 对方公钥
     * @return KeyPair
     * @throws DhException DH 异常
     */
    public static KeyPair generateKeyPair(PublicKey publicKey) throws DhException {
        if (BaseUtils.isEmpty(publicKey)) {
            throw new DhException("publicKey 不能为空！");
        }
        if (!(publicKey instanceof DHPublicKey)) {
            throw new DhException("publicKey 必须是 DH 公钥");
        }
        try {
            DHParameterSpec params = ((DHPublicKey) publicKey).getParams();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("DH");
            generator.initialize(params);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 协商共享密钥
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return 共享密钥字节
     * @throws DhException DH 异常
     */
    public static byte[] generateSecret(PrivateKey privateKey, PublicKey publicKey) throws DhException {
        if (BaseUtils.isEmpty(privateKey) || BaseUtils.isEmpty(publicKey)) {
            throw new DhException("privateKey | publicKey 不能为空！");
        }
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 协商共享密钥并转为指定对称算法的密钥
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @param algorithm  目标对称算法，如 AES
     * @param keyBytes   密钥字节长度，如 16
     * @return 对称加密密钥
     * @throws DhException DH 异常
     */
    public static SecretKey generateSecretKey(PrivateKey privateKey, PublicKey publicKey, String algorithm, int keyBytes)
            throws DhException {
        byte[] secret = generateSecret(privateKey, publicKey);
        byte[] key = new byte[keyBytes];
        System.arraycopy(secret, 0, key, 0, Math.min(secret.length, keyBytes));
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 公钥转 Base64 字符串
     *
     * @param publicKey 公钥对象
     * @return 公钥字符串
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 私钥转 Base64 字符串
     *
     * @param privateKey 私钥对象
     * @return 私钥字符串
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * Base64 字符串转公钥对象
     *
     * @param publicKey 公钥字符串
     * @return 公钥对象
     * @throws DhException DH 异常
     */
    public static PublicKey getPublicKey(String publicKey) throws DhException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * Base64 字符串转私钥对象
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws DhException DH 异常
     */
    public static PrivateKey getPrivateKey(String privateKey) throws DhException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 协商共享密钥（Base64 形式）
     *
     * @param privateKey 本方私钥（Base64）
     * @param publicKey  对方公钥（Base64）
     * @return 共享密钥 Base64 字符串
     * @throws DhException DH 异常
     */
    public static String generateSecretStr(String privateKey, String publicKey) throws DhException {
        return Base64.encodeBase64String(generateSecret(getPrivateKey(privateKey), getPublicKey(publicKey)));
    }

}
