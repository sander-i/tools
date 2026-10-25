package org.tools.encrypt.digitalsignature.dsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.EncryptException;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;

/**
 * DSA 数字签名工具。
 *
 * <p>默认签名算法为 {@code SHA256withDSA}，默认密钥 1024 位；
 * 公钥/私钥为 Base64(X.509/PKCS#8) 字符串。</p>
 *
 * <p>线程安全：{@code sign}/{@code verify} 每次调用使用独立 Signature 实例，可并发调用
 * （此前共享静态 Signature，已修复）。</p>
 *
 * @author sander 2024/04/14
 */
@Slf4j
public class DsaHelper {

    private DsaHelper() {
    }

    @Getter
    private static DsaProperties properties;

    /**
     * 装载 KeyPair 实体配置（按 Base64 字符串解析公私钥对象）
     *
     * @param keyPair 密钥对配置
     * @throws EncryptException 配置为空或密钥解析失败时抛出
     * @throws NoSuchAlgorithmException 密钥算法不可用时抛出
     */
    public static void load(KeyPair keyPair) throws EncryptException, NoSuchAlgorithmException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EncryptException("keyPair 不能为空！");
        }
        DsaProperties properties = new DsaProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        properties.setSignAlgorithm(keyPair.getSignAlgorithm());
        try {
            // load(DsaProperties) 校验的是密钥对象字段，此处需从字符串解析
            properties.setPublicKey(DsaKeyGenerator.getPublicKey(AlgorithmEnum.DSA.getAlgorithm(), keyPair.getPublicKey()));
            properties.setPrivateKey(DsaKeyGenerator.getPrivateKey(AlgorithmEnum.DSA.getAlgorithm(), keyPair.getPrivateKey()));
        } catch (InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
        load(properties);
    }

    /**
     * 装载 DSA 配置，并预检签名算法可用性
     *
     * @param properties DSA 配置
     * @throws EncryptException 配置为空或公私钥缺失时抛出
     * @throws NoSuchAlgorithmException 签名算法不可用时抛出
     */
    public static void load(DsaProperties properties) throws EncryptException, NoSuchAlgorithmException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EncryptException("properties 不能为空！");
        }
        if (!BaseUtils.isNotEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
            throw new EncryptException("publicKey|privateKey 不能为空！");
        }
        DsaHelper.properties = properties;
        // 预检签名算法可用性（fail-fast）；SignatureUtils 每次调用创建独立实例，这里不保留共享对象
        Signature.getInstance(properties.getSignAlgorithm());
    }

    /**
     * 使用全局激活配置签名
     *
     * @param wrought 待签名的数据
     * @return Base64 编码的签名结果
     * @throws EncryptException 签名失败时抛出
     */
    public static String sign(String wrought) throws EncryptException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), properties.getPrivateKey(), wrought);
    }

    /**
     * 使用全局激活配置验签
     *
     * @param wrought 待验签的原始数据
     * @param sign    待验证的 Base64 编码签名
     * @return 验证通过返回 true，否则返回 false
     * @throws EncryptException 验证失败时抛出
     */
    public static boolean verify(String wrought, String sign) throws EncryptException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), properties.getPublicKey(), wrought, sign);
    }

    /**
     * 使用指定 Base64(PKCS#8) 私钥字符串签名
     *
     * @param privateKey Base64 编码的 PKCS#8 私钥字符串
     * @param wrought    待签名的数据
     * @return Base64 编码的签名结果
     * @throws EncryptException 密钥解析失败或签名失败时抛出
     */
    public static String sign(String privateKey, String wrought) throws EncryptException {
        String signText;
        try {
            // KeyFactory 需要"密钥算法名"（DSA），而非签名算法名——此前传签名算法名导致 NoSuchAlgorithmException
            signText = sign(DsaKeyGenerator.getPrivateKey(AlgorithmEnum.DSA.getAlgorithm(), privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EncryptException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
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
     * @throws EncryptException 密钥解析失败或验证失败时抛出
     */
    public static boolean verify(String publicKey, String wrought, String sign) throws EncryptException {
        try {
            // 同 sign：KeyFactory 需要密钥算法名 DSA
            return verify(DsaKeyGenerator.getPublicKey(AlgorithmEnum.DSA.getAlgorithm(), publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

    /**
     * 使用指定私钥对象签名
     *
     * @param privateKey 私钥对象
     * @param wrought    待签名的数据
     * @return Base64 编码的签名结果
     * @throws EncryptException 签名失败时抛出
     */
    public static String sign(PrivateKey privateKey, String wrought) throws EncryptException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
    }

    /**
     * 使用指定公钥对象验签
     *
     * @param publicKey 公钥对象
     * @param wrought   待验签的原始数据
     * @param sign      待验证的 Base64 编码签名
     * @return 验证通过返回 true，否则返回 false
     * @throws EncryptException 验证失败时抛出
     */
    public static boolean verify(PublicKey publicKey, String wrought, String sign) throws EncryptException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
    }

}
