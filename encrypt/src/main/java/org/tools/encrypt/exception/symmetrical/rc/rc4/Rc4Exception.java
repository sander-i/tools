package org.tools.encrypt.exception.symmetrical.rc.rc4;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * RC4 加解密异常。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc4Exception extends SymmetricalException {
    private static final long serialVersionUID = 4192357186042873315L;

    public Rc4Exception() {
    }

    public Rc4Exception(String error) {
        super(error);
    }

    public Rc4Exception(Exception exception) {
        super(exception);
    }

    public Rc4Exception(String error, Exception exception) {
        super(error, exception);
    }
}
