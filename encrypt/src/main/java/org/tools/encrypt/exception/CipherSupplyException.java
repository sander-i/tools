package org.tools.encrypt.exception;

import org.tools.bedrock.exception.ErrorEnum;

/**
 * @author sander
 * @version 1.0
 */
public class CipherSupplyException extends EncryptException {

    public CipherSupplyException() {
    }

    public CipherSupplyException(String error) {
        super(error);
    }

    public CipherSupplyException(Exception exception) {
        super(exception);
    }

    public CipherSupplyException(String error, Exception exception) {
        super(error, exception);
    }

    public CipherSupplyException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
