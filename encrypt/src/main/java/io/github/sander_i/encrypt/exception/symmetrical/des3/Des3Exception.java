package io.github.sander_i.encrypt.exception.symmetrical.des3;

import io.github.sander_i.bedrock.exception.ErrorEnum;
import io.github.sander_i.encrypt.exception.symmetrical.SymmetricalException;

/**
 * 3DES 加解密异常
 *
 * @author sander
 * @version 1.0
 */
public class Des3Exception extends SymmetricalException {

    private static final long serialVersionUID = 2739154826173049518L;

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

    public Des3Exception(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
