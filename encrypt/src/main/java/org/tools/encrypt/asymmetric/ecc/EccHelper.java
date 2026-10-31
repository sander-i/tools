package org.tools.encrypt.asymmetric.ecc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.asymmetric.Ecc;
import org.tools.encrypt.entity.asymmetric.EccProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.asymmetric.ecc.EccException;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.KeyAgreement;

/**
 * ECC 椭圆曲线密钥协商工具（ECDH）。
 *
 * <p>使用 JDK 默认 Provider（SunEC）完成 ECDH 协商，默认曲线 {@code secp256r1}（P-256），
 * 共享秘密长度为 32 字节；{@code secp384r1} 为 48 字节，{@code secp521r1} 为 66 字节。</p>
 *
 * <p><b>安全提示：</b>{@code generateSharedSecret} 返回的是 ECDH 原始共享秘密，
 * 未经 KDF 直接使用会产生偏差与弱密钥风险。调用方应至少使用 HKDF（或 SHA-256）
 * 结合双方公钥、盐值与上下文信息派生会话密钥，不要直接把原始字节当作 AES 密钥。</p>
 *
 * <p>本包只提供 ECDH 密钥协商，不提供 ECIES 加解密：JDK 8 默认 JCE 策略下
 * {@code Cipher.getMaxAllowedKeyLength("ECIES")} 为 128，P-256 会被拒绝；
 * BouncyCastle 的 ECIES 又不接受 SunEC 生成的密钥。</p>
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
@Slf4j
public class EccHelper {
    /**
     * 密钥协商算法名
     */
    private static final String KEY_AGREEMENT_ALGORITHM = "ECDH";

    /**
     * 当前激活的 ECC 配置
     */
    @Getter
    private static EccProperties properties = new EccProperties();

    private EccHelper() {
    }

    /**
     * 按分组名装载 ECC 配置（实体需为 Ecc，分组按 name 匹配）
     *
     * <p>曲线优先级：分组自身为 {@link Ecc} 且配置了曲线时使用分组曲线，
     * 否则回退到顶层 {@link Ecc} 的 {@code curve} 字段。</p>
     *
     * @param keyPair 密钥对配置
     * @param name    分组名
     * @throws EccException 配置为空、实体类型不符或密钥解析失败时抛出
     */
    public static void load(KeyPair keyPair, String name) throws EccException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EccException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Ecc)) {
            throw new EccException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Ecc ！");
        }
        Ecc ecc = (Ecc) keyPair;
        if (Boolean.TRUE.equals(ecc.getGroupEnabled())) {
            List<KeyPair> groupList = ecc.getGroup();
            if (BaseUtils.isNotEmpty(groupList)) {
                for (KeyPair group : groupList) {
                    if (name != null && name.equals(group.getName())) {
                        // 分组自身配置了曲线时优先，否则回退顶层 curve
                        String curve = group instanceof Ecc && BaseUtils.isNotEmpty(((Ecc) group).getCurve())
                                ? ((Ecc) group).getCurve() : ecc.getCurve();
                        loadWithCurve(group, curve);
                        return;
                    }
                }
            }
        }
        load(keyPair);
    }

    /**
     * 装载 KeyPair / Ecc 实体配置（Base64 公私钥字符串解析为密钥对象）
     *
     * @param keyPair 密钥对配置
     * @throws EccException 配置或密钥为空、密钥解析失败时抛出
     */
    public static void load(KeyPair keyPair) throws EccException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EccException("keyPair 不能为空！");
        }
        String curve = keyPair instanceof Ecc ? ((Ecc) keyPair).getCurve() : null;
        loadWithCurve(keyPair, curve);
    }

    /**
     * 按指定曲线兜底装载 KeyPair 配置
     *
     * @param keyPair 密钥对配置
     * @param curve   曲线名（为空时使用默认 secp256r1）
     * @throws EccException 配置或密钥为空、密钥解析失败时抛出
     */
    private static void loadWithCurve(KeyPair keyPair, String curve) throws EccException {
        if (BaseUtils.isEmpty(keyPair.getPublicKey(), keyPair.getPrivateKey())) {
            throw new EccException("publicKey|privateKey 不能为空！");
        }
        EccProperties eccProperties = new EccProperties();
        String algorithm = BaseUtils.isNotEmpty(keyPair.getAlgorithm())
                ? keyPair.getAlgorithm() : AlgorithmEnum.EC.getAlgorithm();
        eccProperties.setAlgorithm(algorithm);
        if (BaseUtils.isNotEmpty(curve)) {
            eccProperties.setCurve(curve);
        }
        if (keyPair.getKeyBits() > 0) {
            eccProperties.setKeyBits(keyPair.getKeyBits());
        }
        eccProperties.setPublicKeyStr(keyPair.getPublicKey());
        eccProperties.setPrivateKeyStr(keyPair.getPrivateKey());
        try {
            eccProperties.setPublicKey(EccKeyGenerator.getPublicKey(algorithm, keyPair.getPublicKey()));
            eccProperties.setPrivateKey(EccKeyGenerator.getPrivateKey(algorithm, keyPair.getPrivateKey()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EccException(e);
        }
        load(eccProperties);
    }

    /**
     * 装载 ECC 配置
     *
     * <p><b>注意</b>：本方法只接受已解析的 {@code PublicKey/PrivateKey} 对象；
     * 仅含 Base64 字符串（{@code publicKeyStr}/{@code privateKeyStr}）的配置请改用
     * {@link #load(KeyPair)}，由它完成字符串解析。</p>
     *
     * @param eccProperties ECC 配置
     * @throws EccException 配置为空、公私钥对象缺失或曲线名为空时抛出
     */
    public static void load(EccProperties eccProperties) throws EccException {
        if (BaseUtils.isEmpty(eccProperties)) {
            throw new EccException("properties 不能为空！");
        }
        // 公钥与私钥必须同时存在（isNotEmpty(varargs) 语义是「任一非空」，不能用于此处）
        if (BaseUtils.isEmpty(eccProperties.getPublicKey()) || BaseUtils.isEmpty(eccProperties.getPrivateKey())) {
            throw new EccException("publicKey|privateKey 不能为空！");
        }
        if (BaseUtils.isEmpty(eccProperties.getCurve())) {
            throw new EccException("curve 不能为空！");
        }
        if (BaseUtils.isEmpty(eccProperties.getAlgorithm())) {
            eccProperties.setAlgorithm(AlgorithmEnum.EC.getAlgorithm());
        }
        EccHelper.properties = eccProperties;
    }

    /**
     * 生成默认曲线（secp256r1）的 ECDH 密钥对
     *
     * @return 密钥对
     * @throws EccException 曲线不可用或密钥生成失败时抛出
     */
    public static java.security.KeyPair generateKeyPair() throws EccException {
        return EccKeyGenerator.generateKeyPair();
    }

    /**
     * 生成指定曲线的 ECDH 密钥对
     *
     * @param curve 曲线名，如 secp256r1 / secp384r1 / secp521r1
     * @return 密钥对
     * @throws EccException 曲线名为空、曲线不存在或密钥生成失败时抛出
     */
    public static java.security.KeyPair generateKeyPair(String curve) throws EccException {
        return EccKeyGenerator.generateKeyPair(curve);
    }

    /**
     * 计算 ECDH 原始共享秘密
     *
     * <p>返回值为协商结果的原始字节（P-256 为 32 字节），<b>不是</b>可直接使用的会话密钥，
     * 调用方必须经过 KDF 派生（如 HKDF / SHA-256 结合双方公钥与盐值）。</p>
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return 原始共享秘密字节
     * @throws EccException 密钥为空、密钥非法（如双方曲线不一致）或协商失败时抛出
     */
    public static byte[] generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws EccException {
        if (BaseUtils.isNull(privateKey, publicKey)) {
            throw new EccException("privateKey|publicKey 不能为空！");
        }
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM);
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret();
        } catch (GeneralSecurityException | IllegalStateException e) {
            // JDK 8 SunEC 对曲线不一致的密钥在 generateSecret 阶段抛 IllegalStateException，
            // JDK 17 则在 doPhase 阶段抛 InvalidKeyException，统一包装为 EccException
            log.error(e.getLocalizedMessage());
            throw new EccException(e);
        }
    }

    /**
     * 计算 ECDH 原始共享秘密并编码为 Base64
     *
     * <p>结果同样需要经过 KDF 派生后才能作为会话密钥使用。</p>
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return Base64 编码的原始共享秘密
     * @throws EccException 密钥为空、密钥非法（如双方曲线不一致）或协商失败时抛出
     */
    public static String generateSharedSecretStr(PrivateKey privateKey, PublicKey publicKey) throws EccException {
        return Base64.encodeBase64String(generateSharedSecret(privateKey, publicKey));
    }

    /**
     * 使用 Base64 公私钥字符串计算 ECDH 原始共享秘密并编码为 Base64
     *
     * <p>私钥为 Base64(PKCS#8)，公钥为 Base64(X.509)；结果需要经过 KDF 派生后才能使用。</p>
     *
     * @param privateKeyStr Base64(PKCS#8) 本方私钥字符串
     * @param publicKeyStr  Base64(X.509) 对方公钥字符串
     * @return Base64 编码的原始共享秘密
     * @throws EccException 字符串为空、解析失败、密钥非法或协商失败时抛出
     */
    public static String generateSharedSecretStr(String privateKeyStr, String publicKeyStr) throws EccException {
        if (BaseUtils.isEmpty(privateKeyStr, publicKeyStr)) {
            throw new EccException("privateKeyStr|publicKeyStr 不能为空！");
        }
        String algorithm = BaseUtils.isNotEmpty(properties.getAlgorithm())
                ? properties.getAlgorithm() : AlgorithmEnum.EC.getAlgorithm();
        try {
            PrivateKey privateKey = EccKeyGenerator.getPrivateKey(algorithm, privateKeyStr);
            PublicKey publicKey = EccKeyGenerator.getPublicKey(algorithm, publicKeyStr);
            return generateSharedSecretStr(privateKey, publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EccException(e);
        }
    }
}
