package io.github.sander_i.encrypt.exception.symmetrical;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 */
public class SymmetricalException extends EncryptException {

    private static final long serialVersionUID = 6591125072110387210L;

    public SymmetricalException() {
    }

    public SymmetricalException(String error) {
        super(error);
    }

    public SymmetricalException(Exception exception) {
        super(exception);
    }

    public SymmetricalException(String error, Exception exception) {
        super(error, exception);
    }

    public SymmetricalException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
