package org.tools.encrypt.digitalsignature.dsa.impl;

import lombok.extern.log4j.Log4j2;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.digitalsignature.dsa.DsaKeyGenerator;
import org.tools.encrypt.digitalsignature.dsa.abst.DsaAbst;
import org.tools.encrypt.exception.digitalsignature.DigitalsignatureException;
import org.tools.encrypt.exception.digitalsignature.dsa.DsaException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:40
 * @description dsa签名实现
 * @scope dsa
 */
@Log4j2
public class DsaImpl extends DsaAbst {

    @Override
    public String encode(String raw) throws DsaException {
        log.info("dsa签名算法不支持加解密操作");
        return null;
    }

    @Override
    public String decode(String wrought) throws DsaException {
        log.info("dsa签名算法不支持加解密操作");
        return null;
    }

    @Override
    public String sign(String wrought) throws DsaException {
        return sign(getDsaProperties().getDsaPrivateKey(), wrought);
    }

    @Override
    public Boolean verify(String wrought, String sign) throws DsaException {
        return verify(getDsaProperties().getDsaPublicKey(), wrought, sign);
    }

    @Override
    public String sign(String privateKey, String wrought) throws DsaException {
        String signText;
        try {
            signText = sign(DsaKeyGenerator.getPrivateKey(privateKey), wrought);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DsaException e) {
            log.error(e.getLocalizedMessage());
            throw new DsaException(e);
        }
        return signText;
    }

    @Override
    public String sign(PrivateKey privateKey, String wrought) throws DsaException {
        try {
            Signature signature = getSignature();
            signature.initSign(privateKey);
            return SignatureUtils.sign(signature, wrought);
        } catch (DigitalsignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DsaException(e);
        }
    }

    @Override
    public Boolean verify(String publicKey, String wrought, String sign) throws DsaException {
        try {
            return verify(DsaKeyGenerator.getPublicKey(publicKey), wrought, sign);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DsaException e) {
            log.error(e.getLocalizedMessage());
            throw new DsaException(e);
        }
    }

    @Override
    public Boolean verify(PublicKey publicKey, String wrought, String sign) throws DsaException {
        try {
            Signature signature = getSignature();
            signature.initVerify(publicKey);
            return SignatureUtils.verify(signature, wrought, sign);
        } catch (DigitalsignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new DsaException(e);
        }
    }

}
