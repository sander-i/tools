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
import java.nio.charset.StandardCharsets;

/**
 * 数字签名通用工具（RSA / DSA 等 JCA Signature 体系的统一入口）。
 *
 * <p>签名值与原文均固定使用 UTF-8 取字节（此前使用 JVM 默认 charset，跨平台签名结果不一致，已修复），
 * 签名输出为标准 Base64。</p>
 *
 * <p>线程安全：每次 {@code sign}/{@code verify} 调用都使用独立的 {@link Signature} 实例
 * （此前共享静态实例，并发调用会互相污染签名状态，已修复）。需要复用实例的高频场景
 * 可自行持有 {@link Signature} 调用对应重载。</p>
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class SignatureUtils {

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
            Signature signature = Signature.getInstance(signAlgorithm);
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
            Signature signature = Signature.getInstance(signAlgorithm);
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
            signature.update(wrought.getBytes(StandardCharsets.UTF_8));
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
            signature.update(wrought.getBytes(StandardCharsets.UTF_8));
            // 验签
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalSignatureException(e);
        }
    }

}
