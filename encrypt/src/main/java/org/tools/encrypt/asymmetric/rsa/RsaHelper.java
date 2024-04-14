package org.tools.encrypt.asymmetric.rsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
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
 * @author sander 2024/03/30
 */
@Slf4j
public class RsaHelper {

    private RsaHelper() {
    }

    @Getter
    private static RsaProperties properties;

    protected static Signature signature;
    protected static Cipher publicCipher;
    protected static Cipher privateCipher;

    public static void load(KeyPair keyPair) throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EncryptException("keyPair 不能为空！");
        }
        RsaProperties properties = new RsaProperties();
        properties.setPublicKey(keyPair.getPublicKey());
        properties.setPrivateKey(keyPair.getPrivateKey());
        properties.setRsaPublicKey(RsaKeyGenerator.getPublicKey(keyPair.getPublicKey()));
        properties.setRsaPrivateKey(RsaKeyGenerator.getPrivateKey(keyPair.getPrivateKey()));
        properties.setIvParameter(keyPair.getIvParameter());
        load(properties);
    }

    public static void load(RsaProperties properties) throws EncryptException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EncryptException("properties 不能为空！");
        }
        if (!BaseUtils.isNotEmpty(properties.getRsaPublicKey(), properties.getRsaPrivateKey())) {
            throw new EncryptException("publicKey|privateKey 不能为空！");
        }
        RsaHelper.properties = properties;
        if (BaseUtils.isNull(signature) ||
                // 签名算法必须与配置文件一致
                !Objects.equals(signature.getAlgorithm(), properties.getSignAlgorithm())) {
            signature = Signature.getInstance(properties.getSignAlgorithm());
        }

        if (BaseUtils.isNull(publicCipher, privateCipher) ||
                // 公私钥算法必须一致
                !Objects.equals(publicCipher.getAlgorithm(), privateCipher.getAlgorithm()) ||
                // 密钥算法与配置文件必须一致
                !Objects.equals(publicCipher.getAlgorithm(), properties.getSignAlgorithm())) {
            publicCipher = Cipher.getInstance(properties.getAlgorithmMode());
            privateCipher = Cipher.getInstance(properties.getAlgorithmMode());
        }
    }

    public static String encode(String raw) throws RsaException {
        return RsaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getRsaPublicKey(), properties.getIvParameter(), raw, properties.getMaxEncryptBlock());
    }

    public static String decode(String wrought) throws RsaException {
        return RsaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getRsaPrivateKey(), properties.getIvParameter(), wrought, properties.getMaxDecryptBlock());
    }

    public static String encode(String publicKey, String raw) throws RsaException {
        try {
            return encode(RsaKeyGenerator.getPublicKey(publicKey), raw);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    public static String decode(String privateKey, String wrought) throws EncryptException {
        try {
            return decode(RsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

    public static String encode(PublicKey publicKey, String raw) throws RsaException {
        return RsaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), publicKey, properties.getIvParameter(), raw, properties.getMaxEncryptBlock());
    }

    public static String decode(PrivateKey privateKey, String wrought) throws RsaException {
        return RsaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), privateKey, properties.getIvParameter(), wrought, properties.getMaxDecryptBlock());
    }

    public static String sign(String wrought) throws RsaException {
        return RsaUtils.sign(properties.getSignAlgorithm(), properties.getRsaPrivateKey(), wrought);
    }

    public static Boolean verify(String wrought, String sign) throws RsaException {
        return RsaUtils.verify(properties.getSignAlgorithm(), properties.getRsaPublicKey(), wrought, sign);
    }

    public static String sign(String privateKey, String wrought) throws RsaException {
        try {
            return sign(RsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EncryptException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    public static Boolean verify(String publicKey, String wrought, String sign) throws RsaException {
        try {
            return verify(RsaKeyGenerator.getPublicKey(publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    public static String sign(PrivateKey privateKey, String wrought) throws RsaException {
        return RsaUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
    }

    public static Boolean verify(PublicKey publicKey, String wrought, String sign) throws RsaException {
        return RsaUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
    }

}
