package org.tools.encrypt.exception.symmetrical.des3;

import org.tools.encrypt.exception.symmetrical.SymmetricalException;

/**
 * 3DES（DESede）加解密异常。
 *
 * @author sander
 * @since 2.0.1
 */
public class Des3Exception extends SymmetricalException {
    private static final long serialVersionUID = 5127409210471918134L;

    public Des3Exception() {
    }

    public Des3Exception(String error) {
        super(error);
    }

    public Des3Exception(Exception exception) {
        super(exception);
    }

    public Des3Exception(String error, Exception exception) {
        super(error, exception);
    }
}
