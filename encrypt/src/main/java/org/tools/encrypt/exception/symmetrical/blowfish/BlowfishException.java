package org.tools.encrypt.exception.symmetrical.blowfish;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * Blowfish 对称加解密异常。
 *
 * @author sander
 * @since 2.0.1
 */
public class BlowfishException extends SymmetricalException {
    private static final long serialVersionUID = 7316452093041518115L;

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
}
