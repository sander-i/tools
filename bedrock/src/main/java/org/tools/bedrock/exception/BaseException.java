package org.tools.bedrock.exception;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/21 23:15
 * @description 基础异常
 * @scope exception
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = -1681405590078669877L;

    public BaseException() {
    }

    public BaseException(String error) {
        super(error);
    }

    public BaseException(Exception exception) {
        super(exception);
    }

    public BaseException(String error, Exception exception) {
        super(error, exception);
    }

    public BaseException(ErrorEnum error, Exception exception) {
        super(error.getError(), exception);
    }

}
