package org.tools.bedrock.exception;

/**
 * @author sander
 * @version 1.0
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = -1681405590078669877L;

    public BusinessException() {
    }

    public BusinessException(String error) {
        super(error);
    }

    public BusinessException(Exception exception) {
        super(exception);
    }

    public BusinessException(String error, Exception exception) {
        super(error, exception);
    }

}
