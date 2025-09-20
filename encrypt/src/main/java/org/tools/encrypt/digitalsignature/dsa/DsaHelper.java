package org.tools.encrypt.digitalsignature.dsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.digitalsignature.DsaProperties;
import org.tools.encrypt.exception.EncryptException;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
@Slf4j
public class DsaHelper {

    private DsaHelper() {
    }

    @Getter
    private static DsaProperties properties;

    protected static Signature signature;

    public static void load(KeyPair keyPair) throws EncryptException, NoSuchAlgorithmException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EncryptException("keyPair 不能为空！");
        }
        DsaProperties properties = new DsaProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        properties.setSignAlgorithm(keyPair.getSignAlgorithm());
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
        if (BaseUtils.isNull(signature) ||
                // 签名算法必须与配置文件一致
                !Objects.equals(signature.getAlgorithm(), properties.getSignAlgorithm())) {
            signature = Signature.getInstance(properties.getSignAlgorithm());
        }
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
            signText = sign(DsaKeyGenerator.getPrivateKey(properties.getSignAlgorithm(), privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EncryptException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
        return signText;
    }

    public static boolean verify(String publicKey, String wrought, String sign) throws EncryptException {
        try {
            return verify(DsaKeyGenerator.getPublicKey(properties.getSignAlgorithm(), publicKey), wrought, sign);
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
