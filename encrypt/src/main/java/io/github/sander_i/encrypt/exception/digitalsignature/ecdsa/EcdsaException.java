package io.github.sander_i.encrypt.exception.digitalsignature.ecdsa;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.digitalsignature.DigitalSignatureException;

/**
 * ECDSA 签名异常
 *
 * @author sander
 * @version 1.0
 */
public class EcdsaException extends DigitalSignatureException {

    private static final long serialVersionUID = 8401657239184026517L;

    public EcdsaException() {
    }

    public EcdsaException(String error) {
        super(error);
    }

    public EcdsaException(Exception exception) {
        super(exception);
    }

    public EcdsaException(String error, Exception exception) {
        super(error, exception);
    }

    public EcdsaException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
