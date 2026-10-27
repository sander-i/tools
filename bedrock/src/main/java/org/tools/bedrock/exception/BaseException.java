package org.tools.bedrock.exception;
/**
 * 基础异常：承载错误信息与原始异常，是工具模块异常的共同父类。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
public class BaseException extends RuntimeException {
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
