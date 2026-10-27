package org.tools.encrypt.exception;

import org.tools.bedrock.exception.ErrorEnum;
import org.tools.bedrock.exception.ToolsException;

/**
 * 加解密通用异常。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
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
