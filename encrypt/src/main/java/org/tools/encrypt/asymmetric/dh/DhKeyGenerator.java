package org.tools.encrypt.asymmetric.dh;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.asymmetric.DhProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.asymmetric.dh.DhException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * DH 密钥生成工具：密钥对生成、公私钥字符串与对象互转、域参数提取。
 *
 * <p>DH（Diffie-Hellman）是<b>密钥协商</b>算法而非加密算法：本类不提供
 * {@code encode}/{@code decode}。公钥/私钥为 Base64(X.509/PKCS#8) 字符串，
 * 公钥编码中携带域参数（p、g），解析后可经 {@link #getParams(PublicKey)} 取出。</p>
 *
 * <p>默认密钥 2048 位，最小 512 位；位数上限取决于 JDK Provider
 * （JDK 8u121 SunJCE 为 2048，较新 JDK 可达 4096），且必须是 64 的整数倍，
 * 越界会由 JCE 抛出 {@link InvalidParameterException} 并被包装为 {@link DhException}。</p>
 *
 * <p>线程安全：{@code generateKeyPair} 每次创建独立 {@link KeyPairGenerator}；
 * 默认密钥对采用双检锁懒加载缓存，缓存对象不可变，可安全共享。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@Slf4j
public class DhKeyGenerator {
    /**
     * 默认密钥位数
     */
    private static final int DEFAULT_KEY_BITS = 2048;

    /**
     * 允许的最小密钥位数
     */
    private static final int MIN_KEY_BITS = 512;

    /**
     * 锁对象
     */
    private static final Object LOCK = new Object();

    /**
     * Dh 配置文件
     */
    private static volatile DhProperties properties;

    /**
     * 默认密钥对（懒加载缓存）
     */
    private static volatile KeyPair keyPair;

    private DhKeyGenerator() {
    }

    /**
     * 生成默认 2048 位 DH 密钥对
     *
     * @return 密钥对
     * @throws DhException 算法不可用或密钥生成失败时抛出
     */
    public static KeyPair generateKeyPair() throws DhException {
        return generateKeyPair(DEFAULT_KEY_BITS);
    }

    /**
     * 生成指定密钥位数的 DH 密钥对（每次随机生成新的域参数，开销较大）
     *
     * @param keyBits 密钥位数，不能小于 512，且须为 64 的整数倍
     * @return 密钥对
     * @throws DhException 位数非法、算法不可用或密钥生成失败时抛出
     */
    public static KeyPair generateKeyPair(int keyBits) throws DhException {
        if (keyBits < MIN_KEY_BITS) {
            throw new DhException("DH 密钥位数不能小于 " + MIN_KEY_BITS + "！");
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(AlgorithmEnum.DH.getAlgorithm());
            keyPairGenerator.initialize(keyBits);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 使用已有域参数生成 DH 密钥对（协商双方需使用同一组参数）
     *
     * @param params DH 域参数
     * @return 密钥对
     * @throws DhException 参数为空、算法不可用或密钥生成失败时抛出
     */
    public static KeyPair generateKeyPair(DHParameterSpec params) throws DhException {
        if (BaseUtils.isNull(params)) {
            throw new DhException("params 不能为空！");
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(AlgorithmEnum.DH.getAlgorithm());
            keyPairGenerator.initialize(params);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            log.error(e.getLocalizedMessage());
            throw new DhException(e);
        }
    }

    /**
     * 获取默认密钥对的公钥
     *
     * @return 公钥对象
     * @throws DhException 密钥生成失败时抛出
     */
    public static PublicKey getPublicKey() throws DhException {
        return getKeyPair().getPublic();
    }

    /**
     * 获取默认密钥对的私钥
     *
     * @return 私钥对象
     * @throws DhException 密钥生成失败时抛出
     */
    public static PrivateKey getPrivateKey() throws DhException {
        return getKeyPair().getPrivate();
    }

    /**
     * 获取默认公钥的 Base64(X.509) 字符串
     *
     * @return 公钥字符串
     * @throws DhException 密钥生成失败时抛出
     */
    public static String getPublicKeyStr() throws DhException {
        return getPublicKeyStr(getKeyPair().getPublic());
    }

    /**
     * 获取默认私钥的 Base64(PKCS#8) 字符串
     *
     * @return 私钥字符串
     * @throws DhException 密钥生成失败时抛出
     */
    public static String getPrivateKeyStr() throws DhException {
        return getPrivateKeyStr(getKeyPair().getPrivate());
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
     * 由 Base64(X.509) 字符串解析公钥
     *
     * @param algorithm <b>密钥算法名</b>（DH），用于 KeyFactory；请勿传签名算法名
     * @param publicKey 公钥字符串
     * @return 公钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PublicKey getPublicKey(String algorithm, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
    }

    /**
     * 由 Base64(PKCS#8) 字符串解析私钥
     *
     * @param algorithm  <b>密钥算法名</b>（DH），用于 KeyFactory；请勿传签名算法名
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     * @throws InvalidKeySpecException  无效的密钥规范异常
     */
    public static PrivateKey getPrivateKey(String algorithm, String privateKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
    }

    /**
     * 提取 DH 公钥携带的域参数
     *
     * @param publicKey DH 公钥对象
     * @return 域参数
     * @throws IllegalArgumentException 公钥不是 {@link DHPublicKey} 时抛出
     */
    public static DHParameterSpec getParams(PublicKey publicKey) {
        if (!(publicKey instanceof DHPublicKey)) {
            throw new IllegalArgumentException("publicKey 必须是 DH 公钥！");
        }
        return ((DHPublicKey) publicKey).getParams();
    }

    /**
     * 获取 Dh 配置文件（首次调用时用默认密钥对填充公私钥与域参数）
     *
     * @return DhProperties
     * @throws DhException 密钥生成或参数提取失败时抛出
     */
    public static DhProperties getProperties() throws DhException {
        initProperties();
        if (BaseUtils.isEmpty(properties.getPublicKeyStr(), properties.getPrivateKeyStr())
                || BaseUtils.isEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
            KeyPair keyPair = getKeyPair();
            properties.setPublicKey(keyPair.getPublic());
            properties.setPrivateKey(keyPair.getPrivate());
            properties.setPublicKeyStr(getPublicKeyStr(keyPair.getPublic()));
            properties.setPrivateKeyStr(getPrivateKeyStr(keyPair.getPrivate()));
            properties.setParams(getParams(keyPair.getPublic()));
        }
        return properties;
    }

    /**
     * 获取默认密钥对（双检锁懒加载）
     *
     * @return 密钥对
     * @throws DhException 密钥生成失败时抛出
     */
    private static KeyPair getKeyPair() throws DhException {
        if (BaseUtils.isNull(keyPair)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(keyPair)) {
                    initProperties();
                    int keyBits = properties.getKeyBits() > 0 ? properties.getKeyBits() : DEFAULT_KEY_BITS;
                    keyPair = generateKeyPair(keyBits);
                }
            }
        }
        return keyPair;
    }

    /**
     * 单例模式-线程安全
     */
    private static void initProperties() {
        if (BaseUtils.isNull(properties)) {
            synchronized (LOCK) {
                if (BaseUtils.isNull(properties)) {
                    properties = new DhProperties();
                }
            }
        }
    }
}
