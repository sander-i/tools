package org.tools.encrypt.digitalsignature;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.tools.encrypt.exception.digitalsignature.DigitalsignatureException;

import java.security.Signature;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 16:59
 * @description 数字签名
 * @scope digitalsignature
 */
@Log4j2
public class SignatureUtils {

    private SignatureUtils() {
    }

    public static String sign(Signature signature, String wrought) throws DigitalsignatureException {
        String signText;
        try {
            signature.update(wrought.getBytes());
            // 签名
            signText = new String(Base64.encodeBase64(signature.sign()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalsignatureException(e);
        }
        return signText;
    }

    public static Boolean verify(Signature signature, String wrought, String sign) throws DigitalsignatureException {
        try {
            signature.update(wrought.getBytes());
            // 验签
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new DigitalsignatureException(e);
        }
    }

}
