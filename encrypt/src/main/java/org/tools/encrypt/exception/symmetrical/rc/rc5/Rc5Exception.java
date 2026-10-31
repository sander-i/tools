package org.tools.encrypt.exception.symmetrical.rc.rc5;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * RC5 对称加解密异常。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc5Exception extends SymmetricalException {
    private static final long serialVersionUID = 5129874302154729913L;

    public Rc5Exception() {
    }

    public Rc5Exception(String error) {
        super(error);
    }

    public Rc5Exception(Exception exception) {
        super(exception);
    }

    public Rc5Exception(String error, Exception exception) {
        super(error, exception);
    }
}
