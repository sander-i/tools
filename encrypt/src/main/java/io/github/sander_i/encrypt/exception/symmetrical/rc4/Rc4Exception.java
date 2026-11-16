package io.github.sander_i.encrypt.exception.symmetrical.rc4;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException;

/**
 * RC4 加解密异常
 *
 * @author sander
 * @version 1.0
 */
public class Rc4Exception extends SymmetricalException {

    private static final long serialVersionUID = -8023175462190428376L;

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

    public Rc4Exception(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
