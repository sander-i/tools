package io.github.sander_i.encrypt.exception.symmetrical.rc2;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException;

/**
 * RC2 加解密异常
 *
 * @author sander
 * @version 1.0
 */
public class Rc2Exception extends SymmetricalException {

    private static final long serialVersionUID = 6517234518237148201L;

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

    public Rc2Exception(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
