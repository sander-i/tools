package org.tools.bedrock.http.resp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/02/12
 */
@Getter
@RequiredArgsConstructor
public enum RS implements S {

    OK("0", "成功"),

    SUCCESS("1000", "请求成功"),

    FAIL("1001", "请求失败"),

    ERROR("1002", "内部错误"),

    ARGUMENT_ABSENCE("1003", "参数缺失"),

    ARGUMENT_ILLEGAL("1004", "参数错误"),

    DATA_DUPLICATE("1005", "数据重复"),

    NO_PERMISSION("1006", "没有权限"),

    GET_LOCK_ERROR("1007", "获取分布式锁失败");

    private final String code;
    private final String message;

}
