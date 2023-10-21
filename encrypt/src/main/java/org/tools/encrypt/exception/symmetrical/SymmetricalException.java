package org.tools.encrypt.exception.symmetrical;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/07 23:32
 * @description 对称加密异常
 * @scope encrypt
 */
public class SymmetricalException extends EncryptException {

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
