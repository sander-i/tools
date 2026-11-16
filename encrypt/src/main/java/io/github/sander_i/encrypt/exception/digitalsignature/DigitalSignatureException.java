package io.github.sander_i.encrypt.exception.digitalsignature;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 */
public class DigitalSignatureException extends EncryptException {

    private static final long serialVersionUID = -5287092327897656647L;

    public DigitalSignatureException() {
    }

    public DigitalSignatureException(String error) {
        super(error);
    }

    public DigitalSignatureException(Exception exception) {
        super(exception);
    }

    public DigitalSignatureException(String error, Exception exception) {
        super(error, exception);
    }

    public DigitalSignatureException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
