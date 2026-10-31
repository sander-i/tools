package org.tools.encrypt.digitalsignature.ecdsa;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.digitalsignature.EcdsaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.InvalidAlgorithmParameterException;
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
 * ECDSA 密钥生成工具。
 *
 * <p>密钥算法为 {@code EC}，默认椭圆曲线 {@code secp256r1}（256 位）；
 * 公钥/私钥为 Base64(X.509/PKCS#8) 字符串。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class EcdsaKeyGenerator {
    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    @Getter
    @Setter
    private static String algorithm = AlgorithmEnum.EC.getAlgorithm();

    /**
     * 椭圆曲线名称
     */
    @Getter
    private static volatile String curve = "secp256r1";

    /**
     * Ecdsa 配置文件
     */
    private static volatile EcdsaProperties properties;

    /**
     * 密钥对生成器
     */
    private static volatile KeyPairGenerator keyPairGenerator;

    /**
     * 密钥对
     */
    private static volatile KeyPair keyPair;

    private EcdsaKeyGenerator() {
    }

    /**
     * 设置椭圆曲线名称。
     *
     * <p>曲线变化时会清空已缓存的密钥对与配置，使新曲线在下次生成密钥时生效；
     * 传入空值或与当前曲线相同时不产生任何副作用。</p>
     *
     * @param curve 曲线名，如 secp256r1 / secp384r1 / secp521r1
     */
    public static void setCurve(String curve) {
        if (BaseUtils.isEmpty(curve)) {
            return;
        }
        synchronized (LOCK) {
            if (curve.equals(EcdsaKeyGenerator.curve)) {
                return;
            }
            EcdsaKeyGenerator.curve = curve;
            // 曲线变化后旧密钥对与配置不再适用，清空缓存以便按新曲线重新生成
            keyPair = null;
            keyPairGenerator = null;
            properties = null;
        }
    }

    /**
     * 初始化密钥生成器对象
     *
     * @throws EcdsaException 密钥生成异常
     */
    private static void init() throws EcdsaException {
        if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
            // 线程安全
            synchronized (LOCK) {
                if (BaseUtils.isNull(keyPairGenerator, keyPair)) {
                    try {
                        keyPairGenerator = EncryptKeyGenerator.getKeyPairGenerator(algorithm);
                        keyPairGenerator.initialize(new ECGenParameterSpec(curve));
                        keyPair = keyPairGenerator.generateKeyPair();
                    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
                        log.error(e.getLocalizedMessage());
                        throw new EcdsaException(e);
                    }
                }
            }
        }
    }

    /**
     * 获取密钥对象
     *
     * @return KeyPair 密钥对
     * @throws EcdsaException 密钥生成异常
     */
    public static KeyPair getKeyPair() throws EcdsaException {
        init();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @return PublicKey 公钥对象
     * @throws EcdsaException 密钥生成异常
     */
    public static PublicKey getPublicKey() throws EcdsaException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return PrivateKey 私钥对象
     * @throws EcdsaException 密钥生成异常
     */
    public static PrivateKey getPrivateKey() throws EcdsaException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取公钥
     *
     * @return String Base64(X.509) 公钥字符串
     * @throws EcdsaException 密钥生成异常
     */
    public static String getPublicKeyStr() throws EcdsaException {
        return Base64.encodeBase64String(getKeyPair().getPublic().getEncoded());
    }

    /**
     * 获取私钥
     *
     * @return String Base64(PKCS#8) 私钥字符串
     * @throws EcdsaException 密钥生成异常
     */
    public static String getPrivateKeyStr() throws EcdsaException {
        return Base64.encodeBase64String(getKeyPair().getPrivate().getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥对象
     * @return String Base64(X.509) 公钥字符串
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥对象
     * @return String Base64(PKCS#8) 私钥字符串
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param algorithm <b>密钥算法名</b>（EC），用于 KeyFactory；请勿传签名算法名（如 SHA256withECDSA），
     *                  否则 KeyFactory 解析失败抛 NoSuchAlgorithmException
     * @param publicKey 公钥字符串
     * @return PublicKey 公钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PublicKey getPublicKey(String algorithm, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param algorithm  <b>密钥算法名</b>（EC），用于 KeyFactory；请勿传签名算法名
     * @param privateKey 私钥字符串
     * @return PrivateKey 私钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PrivateKey getPrivateKey(String algorithm, String privateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(privateKey);
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
                    properties = new EcdsaProperties();
                }
            }
        }
    }

    /**
     * 获取 Ecdsa 配置文件
     *
     * @return EcdsaProperties ECDSA 配置
     * @throws EcdsaException 密钥生成异常
     */
    public static EcdsaProperties getEcdsaProperties() throws EcdsaException {
        initProperties();
        // 回填当前生效的曲线，保证配置与实际生成的密钥对一致
        properties.setCurve(curve);

        if (BaseUtils.isEmpty(properties.getPublicKeyStr(), properties.getPrivateKeyStr()) ||
                BaseUtils.isEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
            properties.setPublicKey(EcdsaKeyGenerator.getPublicKey());
            properties.setPrivateKey(EcdsaKeyGenerator.getPrivateKey());
            properties.setPublicKeyStr(EcdsaKeyGenerator.getPublicKeyStr(properties.getPublicKey()));
            properties.setPrivateKeyStr(EcdsaKeyGenerator.getPrivateKeyStr(properties.getPrivateKey()));
        }
        return properties;
    }
}
