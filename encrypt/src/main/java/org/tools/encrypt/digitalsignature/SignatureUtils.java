package org.tools.encrypt.digitalsignature;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Objects;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class SignatureUtils {

    protected static Signature signature;

    /**
     * 签名
     *
     * @param signAlgorithm 签名算法
     * @param privateKey    私钥
     * @param wrought       明文
     * @return 签名
     * @throws RsaException 异常
     */
    public static String sign(String signAlgorithm, PrivateKey privateKey, String wrought) throws DigitalSignatureException {
        try {
            if (Objects.isNull(signature) || !Objects.equals(signAlgorithm, signature.getAlgorithm())) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initSign(privateKey);
            return sign(signature, wrought);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalSignatureException(e);
        }
    }

    /**
     * 验签
     *
     * @param signAlgorithm 签名算法
     * @param publicKey     公钥
     * @param wrought       明文
     * @param sign          签名
     * @return 是否验签通过
     * @throws RsaException 异常
     */
    public static boolean verify(String signAlgorithm, PublicKey publicKey, String wrought, String sign) throws DigitalSignatureException {
        try {
            if (Objects.isNull(signature) || !Objects.equals(signAlgorithm, signature.getAlgorithm())) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initVerify(publicKey);
            return verify(signature, wrought, sign);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalSignatureException(e);
        }
    }

    /**
     * 签名
     *
     * @param signature 签名
     * @param wrought   明文
     * @return 签名
     * @throws DigitalSignatureException 异常
     */
    public static String sign(Signature signature, String wrought) throws DigitalSignatureException {
        String signText;
        try {
            signature.update(wrought.getBytes());
            // 签名
            signText = new String(Base64.encodeBase64(signature.sign()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalSignatureException(e);
        }
        return signText;
    }

    /**
     * 验签
     *
     * @param signature 签名
     * @param wrought   明文
     * @param sign      签名
     * @return 是否验签通过
     * @throws DigitalSignatureException 异常
     */
    public static boolean verify(Signature signature, String wrought, String sign) throws DigitalSignatureException {
        try {
            signature.update(wrought.getBytes());
            // 验签
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalSignatureException(e);
        }
    }

}
