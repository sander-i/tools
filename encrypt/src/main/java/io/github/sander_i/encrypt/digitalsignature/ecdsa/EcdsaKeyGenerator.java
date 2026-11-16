package io.github.sander_i.encrypt.digitalsignature.ecdsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * ECDSA 密钥生成工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class EcdsaKeyGenerator {

    private EcdsaKeyGenerator() {
    }

    /**
     * 默认曲线：P-256，对应 SHA256withECDSA
     */
    public static final String DEFAULT_CURVE = "secp256r1";

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * 密钥对生成器
     */
    private static KeyPairGenerator keyPairGenerator;

    /**
     * 当前曲线
     */
    private static String currentCurve;

    /**
     * 初始化密钥对生成器
     *
     * @param curve 椭圆曲线名称
     * @throws EcdsaException ECDSA 异常
     */
    private static void init(String curve) throws EcdsaException {
        if (BaseUtils.isNull(keyPairGenerator) || !curve.equals(currentCurve)) {
            synchronized (LOCK) {
                try {
                    keyPairGenerator = KeyPairGenerator.getInstance(AlgorithmEnum.EC.getAlgorithm());
                    keyPairGenerator.initialize(new ECGenParameterSpec(curve));
                    currentCurve = curve;
                } catch (NoSuchAlgorithmException | java.security.InvalidAlgorithmParameterException e) {
                    log.error(e.getLocalizedMessage());
                    throw new EcdsaException(e);
                }
            }
        }
    }

    /**
     * 使用默认曲线生成密钥对
     *
     * @return KeyPair
     * @throws EcdsaException ECDSA 异常
     */
    public static KeyPair getKeyPair() throws EcdsaException {
        return getKeyPair(DEFAULT_CURVE);
    }

    /**
     * 按指定曲线生成密钥对
     *
     * @param curve 椭圆曲线名称
     * @return KeyPair
     * @throws EcdsaException ECDSA 异常
     */
    public static KeyPair getKeyPair(String curve) throws EcdsaException {
        init(curve);
        return keyPairGenerator.generateKeyPair();
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
     * @throws EcdsaException ECDSA 异常
     */
    public static PublicKey getPublicKey(String publicKey) throws EcdsaException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(AlgorithmEnum.EC.getAlgorithm());
            return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

    /**
     * Base64 字符串转私钥对象
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws EcdsaException ECDSA 异常
     */
    public static PrivateKey getPrivateKey(String privateKey) throws EcdsaException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(AlgorithmEnum.EC.getAlgorithm());
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

}
