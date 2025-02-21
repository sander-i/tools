package org.tools.encrypt.asymmetric.rsa;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.asymmetric.AsymmetricUtils;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.asymmetric.Rsa;
import org.tools.encrypt.entity.asymmetric.RsaProperties;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
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
    private static RsaProperties properties = new RsaProperties();

    protected static Signature signature;
    protected static Cipher publicCipher;
    protected static Cipher privateCipher;

    private static void init(Rsa rsa, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(rsa.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(rsa.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(rsa.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(rsa.getSignAlgorithm());
        }
    }

    public static void load(KeyPair keyPair, String name) throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new RsaException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rsa)) {
            throw new RsaException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rsa ！");
        }
        Rsa rsa = (Rsa) keyPair;
        if (!rsa.getEnabled()) {
            throw new RsaException("Aes enable: false");
        }
        if (rsa.getGroupEnabled()) {
            List<KeyPair> groupList = rsa.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(rsa, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    public static void load(KeyPair keyPair) throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EncryptException("keyPair 不能为空！");
        }
        RsaProperties properties = new RsaProperties();
        properties.setPublicKeyStr(keyPair.getPublicKey());
        properties.setPrivateKeyStr(keyPair.getPrivateKey());
        properties.setPublicKey(RsaKeyGenerator.getPublicKey(keyPair.getPublicKey()));
        properties.setPrivateKey(RsaKeyGenerator.getPrivateKey(keyPair.getPrivateKey()));
        properties.setIvParameter(keyPair.getIvParameter());
        load(properties);
    }

    public static void load(RsaProperties properties) throws EncryptException, NoSuchAlgorithmException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EncryptException("properties 不能为空！");
        }
        if (!BaseUtils.isNotEmpty(properties.getPublicKey(), properties.getPrivateKey())) {
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
        return AsymmetricUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getPublicKey(), properties.getIvParameter(), raw, properties.getMaxEncryptBlock());
    }

    public static String decode(String wrought) throws RsaException {
        return AsymmetricUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getPrivateKey(), properties.getIvParameter(), wrought, properties.getMaxDecryptBlock());
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
        return AsymmetricUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), publicKey, properties.getIvParameter(), raw, properties.getMaxEncryptBlock());
    }

    public static String decode(PrivateKey privateKey, String wrought) throws RsaException {
        return AsymmetricUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), privateKey, properties.getIvParameter(), wrought, properties.getMaxDecryptBlock());
    }

    public static String sign(String wrought) throws RsaException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), properties.getPrivateKey(), wrought);
    }

    public static boolean verify(String wrought, String sign) throws RsaException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), properties.getPublicKey(), wrought, sign);
    }

    public static String sign(String privateKey, String wrought) throws RsaException {
        try {
            return sign(RsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | EncryptException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    public static boolean verify(String publicKey, String wrought, String sign) throws RsaException {
        try {
            return verify(RsaKeyGenerator.getPublicKey(publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    public static String sign(PrivateKey privateKey, String wrought) throws RsaException {
        return SignatureUtils.sign(properties.getSignAlgorithm(), privateKey, wrought);
    }

    public static boolean verify(PublicKey publicKey, String wrought, String sign) throws RsaException {
        return SignatureUtils.verify(properties.getSignAlgorithm(), publicKey, wrought, sign);
    }

}
