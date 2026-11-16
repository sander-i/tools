package io.github.sander_i.encrypt.exception.asymmetric.rsa;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.asymmetric.AsymmetricException;

/**
 * @author sander
 * @version 1.0
 */
public class RsaException extends AsymmetricException {

    public RsaException() {
    }

    public RsaException(String error) {
        super(error);
    }

    public RsaException(Exception exception) {
        super(exception);
    }

    public RsaException(String error, Exception exception) {
        super(error, exception);
    }

    public RsaException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
