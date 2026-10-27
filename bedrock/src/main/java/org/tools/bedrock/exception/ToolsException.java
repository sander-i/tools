package org.tools.bedrock.exception;
/**
 * 工具异常：工具类统一抛出的异常类型。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
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
