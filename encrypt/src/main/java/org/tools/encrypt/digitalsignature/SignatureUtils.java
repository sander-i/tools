package org.tools.encrypt.digitalsignature;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import java.security.Signature;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class SignatureUtils {

    private SignatureUtils() {
    }

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

    public static Boolean verify(Signature signature, String wrought, String sign) throws DigitalSignatureException {
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
