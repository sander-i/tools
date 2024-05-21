package org.tools.encrypt.digitalsignature.dsa;

import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.digitalsignature.SignatureUtils;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.exception.digitalsignature.DigitalSignatureException;

import java.security.*;
import java.util.Objects;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/14
 */
@Slf4j
public class DsaUtils {

    private DsaUtils() {
    }

    protected static Signature signature;

    public static String sign(String signAlgorithm, PrivateKey privateKey, String wrought) throws EncryptException {
        try {
            if (Objects.isNull(signature)) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initSign(privateKey);
            return SignatureUtils.sign(signature, wrought);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

    public static Boolean verify(String signAlgorithm, PublicKey publicKey, String wrought, String sign) throws EncryptException {
        try {
            if (Objects.isNull(signature)) {
                signature = Signature.getInstance(signAlgorithm);
            }
            signature.initVerify(publicKey);
            return SignatureUtils.verify(signature, wrought, sign);
        } catch (DigitalSignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
            throw new EncryptException(e);
        }
    }

}
