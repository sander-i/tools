package io.github.sander_i.encrypt.exception.asymmetric;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 */
public class AsymmetricException extends EncryptException {

    private static final long serialVersionUID = -7692721999904166343L;

    public AsymmetricException() {
    }

    public AsymmetricException(String error) {
        super(error);
    }

    public AsymmetricException(Exception exception) {
        super(exception);
    }

    public AsymmetricException(String error, Exception exception) {
        super(error, exception);
    }

    public AsymmetricException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
