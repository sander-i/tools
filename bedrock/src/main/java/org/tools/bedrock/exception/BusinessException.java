package org.tools.bedrock.exception;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/21 23:16
 * @description 业务异常
 * @scope exception
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
