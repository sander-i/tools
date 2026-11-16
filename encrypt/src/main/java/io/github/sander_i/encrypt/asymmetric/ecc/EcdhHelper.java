package io.github.sander_i.encrypt.asymmetric.ecc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;
import io.github.sander_i.encrypt.exception.asymmetric.ecc.EccException;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * ECDH 密钥协商工具
 *
 * <p>
 * ECC 不直接用于加密数据，而是通过 ECDH 协商出共享密钥，
 * 再用共享密钥调用对称加密（如 AES）处理实际数据。
 * </p>
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class EcdhHelper {

    private EcdhHelper() {
    }

    /**
     * 生成双方的 ECDH 共享密钥
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @return 共享密钥字节
     * @throws EccException ECC 异常
     */
    public static byte[] generateSecret(PrivateKey privateKey, PublicKey publicKey) throws EccException {
        if (BaseUtils.isEmpty(privateKey) || BaseUtils.isEmpty(publicKey)) {
            throw new EccException("privateKey | publicKey 不能为空！");
        }
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance(AlgorithmModeEnum.ECDH.getAlgorithmMode());
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage());
            throw new EccException(e);
        }
    }

    /**
     * 生成双方的 ECDH 共享密钥（Base64 编码）
     *
     * @param privateKey 本方私钥（Base64）
     * @param publicKey  对方公钥（Base64）
     * @return 共享密钥 Base64 字符串
     * @throws EccException ECC 异常
     */
    public static String generateSecretStr(String privateKey, String publicKey) throws EccException {
        byte[] secret = generateSecret(EccKeyGenerator.getPrivateKey(privateKey), EccKeyGenerator.getPublicKey(publicKey));
        return Base64.encodeBase64String(secret);
    }

    /**
     * 协商出共享密钥并转换为指定算法的 SecretKey
     *
     * @param privateKey 本方私钥
     * @param publicKey  对方公钥
     * @param algorithm  目标对称算法，如 AES
     * @param keyBytes   密钥字节长度，如 16
     * @return 对称加密密钥
     * @throws EccException ECC 异常
     */
    public static SecretKey generateSecretKey(PrivateKey privateKey, PublicKey publicKey, String algorithm, int keyBytes)
            throws EccException {
        byte[] secret = generateSecret(privateKey, publicKey);
        byte[] key = new byte[keyBytes];
        System.arraycopy(secret, 0, key, 0, Math.min(secret.length, keyBytes));
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 用默认曲线生成一对可用于 ECDH 的密钥
     *
     * @return KeyPair
     * @throws EccException ECC 异常
     */
    public static KeyPair generateKeyPair() throws EccException {
        return EccKeyGenerator.getKeyPair();
    }

    /**
     * 当前算法名称
     *
     * @return ECDH
     */
    public static String algorithm() {
        return AlgorithmEnum.EC.getAlgorithm();
    }

}
