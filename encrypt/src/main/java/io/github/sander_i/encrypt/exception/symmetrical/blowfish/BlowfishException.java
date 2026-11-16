package io.github.sander_i.encrypt.exception.symmetrical.blowfish;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException;

/**
 * Blowfish 加解密异常
 *
 * @author sander
 * @version 1.0
 */
public class BlowfishException extends SymmetricalException {

    private static final long serialVersionUID = -3318462071840562937L;

    public BlowfishException() {
    }

    public BlowfishException(String error) {
        super(error);
    }

    public BlowfishException(Exception exception) {
        super(exception);
    }

    public BlowfishException(String error, Exception exception) {
        super(error, exception);
    }

    public BlowfishException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
