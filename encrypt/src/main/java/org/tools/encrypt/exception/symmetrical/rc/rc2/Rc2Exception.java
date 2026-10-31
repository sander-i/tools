package org.tools.encrypt.exception.symmetrical.rc.rc2;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * RC2 加解密异常。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc2Exception extends SymmetricalException {
    private static final long serialVersionUID = 6042343578123986221L;

    public Rc2Exception() {
    }

    public Rc2Exception(String error) {
        super(error);
    }

    public Rc2Exception(Exception exception) {
        super(exception);
    }

    public Rc2Exception(String error, Exception exception) {
        super(error, exception);
    }
}
