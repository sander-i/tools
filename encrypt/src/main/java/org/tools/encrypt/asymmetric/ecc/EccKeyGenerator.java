package org.tools.encrypt.asymmetric.ecc;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.EncryptKeyGenerator;
import org.tools.encrypt.entity.asymmetric.EccProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.asymmetric.ecc.EccException;

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
 * ECC 密钥生成工具：ECDH 密钥对生成、公私钥字符串与对象互转。
 *
 * <p>默认曲线 {@code secp256r1}（P-256）；公钥为 Base64(X.509)，私钥为 Base64(PKCS#8)。
 * 本类只服务于 ECDH 密钥协商，不涉及 ECIES 加解密。</p>
 *
 * <p>线程安全：密钥对首次访问时在锁内生成并缓存，之后只读。</p>
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
@Slf4j
public class EccKeyGenerator {
    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * 默认曲线名
     */
    private static final String DEFAULT_CURVE = "secp256r1";

    /**
     * 缓存的默认曲线密钥对
     */
    private static volatile KeyPair keyPair;

    private EccKeyGenerator() {
    }

    /**
     * 生成默认曲线（secp256r1）的 ECDH 密钥对
     *
     * @return 密钥对
     * @throws EccException 曲线不可用或密钥生成失败时抛出
     */
    public static KeyPair generateKeyPair() throws EccException {
        return generateKeyPair(DEFAULT_CURVE);
    }

    /**
     * 生成指定曲线的 ECDH 密钥对
     *
     * @param curve 曲线名，如 secp256r1 / secp384r1
     * @return 密钥对
     * @throws EccException 曲线名为空、曲线不存在或密钥生成失败时抛出
     */
    public static KeyPair generateKeyPair(String curve) throws EccException {
        if (BaseUtils.isEmpty(curve)) {
            throw new EccException("curve 不能为空！");
        }
        try {
            KeyPairGenerator keyPairGenerator = EncryptKeyGenerator.getKeyPairGenerator(AlgorithmEnum.EC
                    .getAlgorithm());
            keyPairGenerator.initialize(new ECGenParameterSpec(curve));
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new EccException(e);
        }
    }

    /**
     * 获取（首次访问时生成）默认曲线的缓存密钥对
     *
     * @return 密钥对
     * @throws EccException 密钥生成失败时抛出
     */
    private static KeyPair getCachedKeyPair() throws EccException {
        if (BaseUtils.isNull(keyPair)) {
            // 线程安全
            synchronized (LOCK) {
                if (BaseUtils.isNull(keyPair)) {
                    keyPair = generateKeyPair();
                }
            }
        }
        return keyPair;
    }

    /**
     * 获取默认曲线密钥对的公钥对象
     *
     * @return 公钥对象
     * @throws EccException 密钥生成失败时抛出
     */
    public static PublicKey getPublicKey() throws EccException {
        return getCachedKeyPair().getPublic();
    }

    /**
     * 获取默认曲线密钥对的私钥对象
     *
     * @return 私钥对象
     * @throws EccException 密钥生成失败时抛出
     */
    public static PrivateKey getPrivateKey() throws EccException {
        return getCachedKeyPair().getPrivate();
    }

    /**
     * 获取默认曲线密钥对的 Base64(X.509) 公钥字符串
     *
     * @return 公钥字符串
     * @throws EccException 密钥生成失败时抛出
     */
    public static String getPublicKeyStr() throws EccException {
        return getPublicKeyStr(getCachedKeyPair().getPublic());
    }

    /**
     * 获取默认曲线密钥对的 Base64(PKCS#8) 私钥字符串
     *
     * @return 私钥字符串
     * @throws EccException 密钥生成失败时抛出
     */
    public static String getPrivateKeyStr() throws EccException {
        return getPrivateKeyStr(getCachedKeyPair().getPrivate());
    }

    /**
     * 将公钥对象编码为 Base64(X.509) 字符串
     *
     * @param publicKey 公钥对象
     * @return 公钥字符串
     */
    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 将私钥对象编码为 Base64(PKCS#8) 字符串
     *
     * @param privateKey 私钥对象
     * @return 私钥字符串
     */
    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * 解析 Base64(X.509) 公钥字符串
     *
     * @param algorithm 密钥算法名（EC），用于 KeyFactory
     * @param publicKey 公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PublicKey getPublicKey(String algorithm, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(publicKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    /**
     * 解析 Base64(PKCS#8) 私钥字符串
     *
     * @param algorithm  密钥算法名（EC），用于 KeyFactory
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PrivateKey getPrivateKey(String algorithm, String privateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] decodedKey = Base64.decodeBase64(privateKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

    /**
     * 获取 ECC 配置（默认曲线密钥对的公私钥对象与字符串均填充）
     *
     * @return ECC 配置
     * @throws EccException 密钥生成失败时抛出
     */
    public static EccProperties getProperties() throws EccException {
        EccProperties properties = new EccProperties();
        KeyPair pair = getCachedKeyPair();
        properties.setPublicKey(pair.getPublic());
        properties.setPrivateKey(pair.getPrivate());
        properties.setPublicKeyStr(getPublicKeyStr(pair.getPublic()));
        properties.setPrivateKeyStr(getPrivateKeyStr(pair.getPrivate()));
        return properties;
    }
}
