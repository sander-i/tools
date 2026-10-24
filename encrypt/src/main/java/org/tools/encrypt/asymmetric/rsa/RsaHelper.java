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
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * RSA 非对称加解密与签名工具。
 *
 * <p>默认加密模式为 {@code RSA/ECB/OAEPWithSHA-256AndMGF1Padding}（BouncyCastle Provider），
 * 默认签名算法为 {@code SHA3-256withRSA}，默认密钥 2048 位。
 * 公钥/私钥为 Base64(X.509/PKCS#8) 字符串；单块明文上限 {@code (keyBits/8) - 66} 字节（OAEP），
 * 超长明文按 {@code maxEncryptBlock} 自动分段。</p>
 *
 * <p>线程安全：{@code encode}/{@code decode}/{@code sign}/{@code verify} 每次调用
 * 均使用独立实例，可并发调用（此前 {@code sign}/{@code verify} 共享静态 Signature，已修复）。</p>
 *
 * @author sander 2024/03/30
 */
@Slf4j
public class RsaHelper {

    private RsaHelper() {
    }

    @Getter
    private static RsaProperties properties = new RsaProperties();

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

    public static void load(KeyPair keyPair, String name) throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new RsaException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Rsa)) {
            throw new RsaException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Rsa ！");
        }
        Rsa rsa = (Rsa) keyPair;
        if (!rsa.getEnabled()) {
            throw new RsaException("RSA enable: false");
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
        // 显式传入的配置（算法/模式/签名算法/密钥位数）覆盖默认值；此前被忽略，导致
        // KeyPair/Rsa 上设置的 signAlgorithm 等不生效。块大小随位数/模式联动重算。
        boolean recalc = false;
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithm())) {
            properties.setAlgorithm(keyPair.getAlgorithm());
        }
        if (BaseUtils.isNotEmpty(keyPair.getAlgorithmMode())) {
            properties.setAlgorithmMode(keyPair.getAlgorithmMode());
            recalc = true;
        }
        if (BaseUtils.isNotEmpty(keyPair.getSignAlgorithm())) {
            properties.setSignAlgorithm(keyPair.getSignAlgorithm());
        }
        if (keyPair.getKeyBits() > 0) {
            properties.setKeyBits(keyPair.getKeyBits());
            recalc = true;
        }
        if (recalc) {
            properties.recalcMaxBlocks();
        }
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
        // 预检签名算法 / 加密模式可用性（fail-fast）；SignatureUtils 与 AsymmetricUtils
        // 每次调用都会创建独立实例，这里不保留共享对象
        Signature.getInstance(properties.getSignAlgorithm());
        Cipher.getInstance(properties.getAlgorithmMode());
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
