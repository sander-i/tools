package org.tools.encrypt.exception.symmetrical.skipjack;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * Skipjack 对称加解密异常。
 *
 * @author sander
 * @since 2.0.1
 */
public class SkipjackException extends SymmetricalException {
    private static final long serialVersionUID = 4021944557903518477L;

    public SkipjackException() {
    }

    public SkipjackException(String error) {
        super(error);
    }

    public SkipjackException(Exception exception) {
        super(exception);
    }

    public SkipjackException(String error, Exception exception) {
        super(error, exception);
    }
}
