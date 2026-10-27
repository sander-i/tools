package org.tools.encrypt.exception.symmetrical;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

/**
 * 对称加解密异常。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
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
