package org.tools.bedrock.http.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/22 0:24
 * @description 结果枚举
 * @scope response
 */
@Getter
@AllArgsConstructor
public enum RE {

    /**
     * 成功
     */
    SUCCESS(1, "success"),
    /**
     * 失败
     */
    FAIL(2, "fail"),
    ;

    private final int code;
    private final String msg;

}
