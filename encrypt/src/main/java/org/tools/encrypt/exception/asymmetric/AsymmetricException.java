package org.tools.encrypt.exception.asymmetric;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

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
