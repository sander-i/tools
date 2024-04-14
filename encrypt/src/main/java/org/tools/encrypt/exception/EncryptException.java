package org.tools.encrypt.exception;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.bedrock.exception.ToolsException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/07
 * @description 加密异常
 * @scope encrypt
 */
public class EncryptException extends ToolsException {

    private static final long serialVersionUID = 5124407319923889191L;

    public EncryptException() {
    }

    public EncryptException(String error) {
        super(error);
    }

    public EncryptException(Exception exception) {
        super(exception);
    }

    public EncryptException(String error, Exception exception) {
        super(error, exception);
    }

    public EncryptException(ErrorEnum error, Exception exception) {
        super(error, exception);
    }

}
