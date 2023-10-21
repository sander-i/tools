package org.tools.bedrock.exception;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/06 23:29
 * @description 工具类异常
 * @scope exception
 */
public class ToolsException extends BaseException {

    private static final long serialVersionUID = -1681405590078669877L;

    public ToolsException() {
    }

    public ToolsException(String error) {
        super(error);
    }

    public ToolsException(Exception exception) {
        super(exception);
    }

    public ToolsException(String error, Exception exception) {
        super(error, exception);
    }

    public ToolsException(ErrorEnum error, Exception exception) {
        super(error.getError(), exception);
    }

}
