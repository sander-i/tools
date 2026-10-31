package org.tools.encrypt.digitalsignature.ecdsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.digitalsignature.EcdsaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;
import org.tools.encrypt.exception.digitalsignature.ecdsa.EcdsaException;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;

/**
 * ECDSA 数字签名工具。
 *
 * <p>默认签名算法为 {@code SHA256withECDSA}，默认密钥曲线 {@code secp256r1}；
 * 公钥/私钥为 Base64(X.509/PKCS#8) 字符串。</p>
 *
 * <p>线程安全：{@code sign}/{@code verify} 每次调用使用独立 Signature 实例，可并发调用。</p>
 *
 * <p>静态配置在类加载时初始化为 {@link EcdsaProperties} 的默认值（默认签名算法
 * {@code SHA256withECDSA}），但默认配置不含任何密钥；未调用 {@code load} 前直接使用
 * {@code sign}/{@code verify} 会因缺少密钥抛出 {@link EcdsaException}。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@Slf4j
public class EcdsaHelper {
    @Getter
    private static EcdsaProperties properties = new EcdsaProperties();

    private EcdsaHelper() {
    }

    /**
     * 装载 KeyPair 实体配置（按 Base64 字符串解析公私钥对象）
     *
     * @param keyPair 密钥对配置
     * @throws EcdsaException 配置为空、公私钥缺失或密钥解析失败时抛出
     */
    public static void load(KeyPair keyPair) throws EcdsaException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EcdsaException("keyPair 不能为空！");
        }
        // 解析前先校验字符串，避免只配置了密钥位数等字段时 Base64 解码 null 引发裸 NPE
        if (BaseUtils.isEmpty(keyPair.getPublicKey()) || BaseUtils.isEmpty(keyPair.getPrivateKey())) {
            throw new EcdsaException("publicKey|privateKey 不能为空！");
        }
        EcdsaProperties properties = new EcdsaProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        // 未指定签名算法时保留 EcdsaProperties 的默认值（SHA256withECDSA），
        // 否则会把 null 写进配置并在 Signature.getInstance 处抛 NPE
        if (BaseUtils.isNotEmpty(keyPair.getSignAlgorithm())) {
            properties.setSignAlgorithm(keyPair.getSignAlgorithm());
        }
        try {
            // load(EcdsaProperties) 校验的是密钥对象字段，此处需从字符串解析
            properties.setPublicKey(EcdsaKeyGenerator.getPublicKey(AlgorithmEnum.EC.getAlgorithm(), keyPair
                    .getPublicKey()));
            properties.setPrivateKey(EcdsaKeyGenerator.getPrivateKey(AlgorithmEnum.EC.getAlgorithm(), keyPair
                    .getPrivateKey()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
        load(properties);
    }

    /**
     * 装载 ECDSA 配置，并预检签名算法可用性
     *
     * @param properties ECDSA 配置
     * @throws EcdsaException 配置为空、公私钥缺失、签名算法为空或不可用时抛出
     */
    public static void load(EcdsaProperties properties) throws EcdsaException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EcdsaException("properties 不能为空！");
        }
        // 公钥与私钥必须同时存在（isNotEmpty(varargs) 语义是「任一非空」，不能用于此处）
        if (BaseUtils.isEmpty(properties.getPublicKey()) || BaseUtils.isEmpty(properties.getPrivateKey())) {
            throw new EcdsaException("publicKey|privateKey 不能为空！");
        }
        // 签名算法为空时 Signature.getInstance(null) 会抛 NPE，此处先给出明确异常
        if (BaseUtils.isEmpty(properties.getSignAlgorithm())) {
            throw new EcdsaException("signAlgorithm 不能为空！");
        }
        // 预检签名算法可用性（fail-fast）；SignatureUtils 每次调用创建独立实例，这里不保留共享对象
        try {
            Signature.getInstance(properties.getSignAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
        // 全部校验通过后再发布配置，避免预检失败时坏配置已生效
        EcdsaHelper.properties = properties;
        // 曲线配置生效：同步到密钥生成器，后续新生成的密钥对使用该曲线
        if (BaseUtils.isNotEmpty(properties.getCurve())) {
            EcdsaKeyGenerator.setCurve(properties.getCurve());
        }
    }

    /**
     * 使用全局激活配置签名
     *
     * @param wrought 待签名的数据
     * @return Base64 编码的签名结果
     * @throws EcdsaException 签名失败时抛出
     */
    public static String sign(String wrought) throws EcdsaException {
        try {
            return SignatureUtils.sign(properties.getSignAlgorithm(), properties.getPrivateKey(), wrought);
        } catch (DigitalSignatureException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

    /**
     * 使用全局激活配置验签
     *
     * @param wrought 待验签的原始数据
     * @param sign    待验证的 Base64 编码签名
     * @return 验证通过返回 true，否则返回 false
     * @throws EcdsaException 验证失败时抛出
     */
    public static boolean verify(String wrought, String sign) throws EcdsaException {
        try {
            return SignatureUtils.verify(properties.getSignAlgorithm(), properties.getPublicKey(), wrought, sign);
        } catch (DigitalSignatureException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

    /**
     * 使用指定 Base64(PKCS#8) 私钥字符串签名
     *
     * @param privateKey Base64 编码的 PKCS#8 私钥字符串
     * @param wrought    待签名的数据
     * @return Base64 编码的签名结果
     * @throws EcdsaException 密钥解析失败或签名失败时抛出
     */
    public static String sign(String privateKey, String wrought) throws EcdsaException {
        String signText;
        try {
            // KeyFactory 需要"密钥算法名"（EC），而非签名算法名
            signText = sign(EcdsaKeyGenerator.getPrivateKey(AlgorithmEnum.EC.getAlgorithm(), privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EcdsaException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
        return signText;
    }

    /**
     * 使用指定 Base64(X.509) 公钥字符串验签
     *
     * @param publicKey Base64 编码的 X.509 公钥字符串
     * @param wrought   待验签的原始数据
     * @param sign      待验证的 Base64 编码签名
     * @return 验证通过返回 true，否则返回 false
     * @throws EcdsaException 密钥解析失败或验证失败时抛出
     */
    public static boolean verify(String publicKey, String wrought, String sign) throws EcdsaException {
        try {
            // 同 sign：KeyFactory 需要密钥算法名 EC
            return verify(EcdsaKeyGenerator.getPublicKey(AlgorithmEnum.EC.getAlgorithm(), publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EcdsaException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

    /**
     * 使用指定私钥对象签名
     *
     * @param privateKey 私钥对象
     * @param wrought    待签名的数据
     * @return Base64 编码的签名结果
     * @throws EcdsaException 签名失败时抛出
     */
    public static String sign(PrivateKey privateKey, String wrought) throws EcdsaException {
        try {
            return SignatureUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
        } catch (DigitalSignatureException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }

    /**
     * 使用指定公钥对象验签
     *
     * @param publicKey 公钥对象
     * @param wrought   待验签的原始数据
     * @param sign      待验证的 Base64 编码签名
     * @return 验证通过返回 true，否则返回 false
     * @throws EcdsaException 验证失败时抛出
     */
    public static boolean verify(PublicKey publicKey, String wrought, String sign) throws EcdsaException {
        try {
            return SignatureUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
        } catch (DigitalSignatureException e) {
            log.error(e.getLocalizedMessage());
            throw new EcdsaException(e);
        }
    }
}
