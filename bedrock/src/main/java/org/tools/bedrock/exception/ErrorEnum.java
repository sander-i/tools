package org.tools.bedrock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误枚举：工具模块错误码与默认错误信息。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {
    /**
     * 工具异常
     */
    TOOL_EXCEPTION("工具类出现异常");

    /**
     * 提示信息
     */
    private final String error;
}
