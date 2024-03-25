package org.tools.encrypt.asymmetric.rsa.impl;

import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;
import org.tools.encrypt.asymmetric.rsa.abst.RsaAbst;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;
import org.tools.encrypt.exception.digitalsignature.DigitalsignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:58
 * @description rsa非对称加密实现
 * @scope rsa
 */
@Deprecated
@Slf4j
public class RsaImpl extends RsaAbst {

    @Override
    public String encode(String raw) throws RsaException {
        return encode(getRsaProperties().getRsaPublicKey(), raw);
    }

    @Override
    public String decode(String wrought) throws RsaException {
        return decode(getRsaProperties().getRsaPrivateKey(), wrought);
    }

    @Override
    public String sign(String wrought) throws RsaException {
        return sign(getRsaProperties().getRsaPrivateKey(), wrought);
    }

    @Override
    public Boolean verify(String wrought, String sign) throws RsaException {
        return verify(getRsaProperties().getRsaPublicKey(), wrought, sign);
    }

    @Override
    public String encode(String publicKey, String raw) throws RsaException {
        try {
            return encode(RsaKeyGenerator.getPublicKey(publicKey), raw);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    @Override
    public String encode(PublicKey publicKey, String raw) throws RsaException {
        // 加密前处理
        byte[] before = raw.getBytes();
        // 执行加密
        byte[] execute;
        try {
            execute = actuator(before, publicKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException | RsaException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
        // 加密后处理
        return encodeBase64(execute);
    }

    @Override
    public String decode(String privateKey, String wrought) throws RsaException {
        try {
            return decode(RsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    @Override
    public String decode(PrivateKey privateKey, String wrought) throws RsaException {
        // 解密前处理
        byte[] before = decodeBase64(wrought);
        // 执行解密
        String execute;
        try {
            execute = actuator(before, privateKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException | RsaException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
        // 解密后处理
        return execute;
    }

    @Override
    public String sign(String privateKey, String wrought) throws RsaException {
        String signText;
        try {
            signText = sign(RsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | RsaException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
        return signText;
    }

    @Override
    public String sign(PrivateKey privateKey, String wrought) throws RsaException {
        try {
            Signature signature = getSignature();
            signature.initSign(privateKey);
            return SignatureUtils.sign(signature, wrought);
        } catch (DigitalsignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    @Override
    public Boolean verify(String publicKey, String wrought, String sign) throws RsaException {
        try {
            return verify(RsaKeyGenerator.getPublicKey(publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }

    @Override
    public Boolean verify(PublicKey publicKey, String wrought, String sign) throws RsaException {
        try {
            Signature signature = getSignature();
            signature.initVerify(publicKey);
            return SignatureUtils.verify(signature, wrought, sign);
        } catch (DigitalsignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new RsaException(e);
        }
    }
}
