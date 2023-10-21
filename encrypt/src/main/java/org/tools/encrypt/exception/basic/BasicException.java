package org.tools.encrypt.exception.basic;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/12 15:30
 * @description 基础加密异常
 * @scope encrypt
 */
public class BasicException extends EncryptException {

    public BasicException() {
    }

    public BasicException(String error) {
        super(error);
    }

    public BasicException(Exception exception) {
        super(exception);
    }

    public BasicException(String error, Exception exception) {
        super(error, exception);
    }

    public BasicException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
