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

    public static String sign(String wrought) throws EncryptException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), properties.getPrivateKey(), wrought);
    }

    public static boolean verify(String wrought, String sign) throws EncryptException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), properties.getPublicKey(), wrought, sign);
    }

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

    public static boolean verify(String publicKey, String wrought, String sign) throws EncryptException {
        try {
            // 同 sign：KeyFactory 需要密钥算法名 DSA
            return verify(DsaKeyGenerator.getPublicKey(AlgorithmEnum.DSA.getAlgorithm(), publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

    public static String sign(PrivateKey privateKey, String wrought) throws EncryptException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
    }

    public static boolean verify(PublicKey publicKey, String wrought, String sign) throws EncryptException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
    }

}
