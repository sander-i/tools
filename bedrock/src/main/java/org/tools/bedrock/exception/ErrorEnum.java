package org.tools.bedrock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sander
 * @version 1.0
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
