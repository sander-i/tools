package org.tools.bedrock.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/06 23:32
 * @description 异常枚举类
 * @scope ToolsException
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
