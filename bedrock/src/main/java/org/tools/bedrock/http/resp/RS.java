package org.tools.bedrock.http.resp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通用响应状态枚举。
 *
 * <p>错误码遵循阿里巴巴《Java 开发手册》错误码规范：五字符格式，首位字母标记错误来源——
 * A 用户端错误、B 系统执行出错、C 调用第三方服务出错，后四位数字为分级编号；
 * 成功态为 {@code "0"}（build 系列方法仅识别本枚举 {@link #OK} 判定 success=true）。
 * 本枚举只收录手册定义的常用一二级宏观码；业务专属错误请实现 {@link S} 自行扩展，
 * 扩展时沿用同结构（如 {@code B0900}），勿复用手册已定义的其他语义。</p>
 *
 * <p>msg 取手册官方描述，个别条目（如 {@link #NOT_LOGIN}）按通用语义微调；
 * 需要向调用方透出具体原因时，用 {@code R.build(s, message)} 传自定义提示。</p>
 *
 * @author sander 2024/02/12
 * @version 2.0
 * @since 1.0.6.3
 */
@Getter
@RequiredArgsConstructor
public enum RS implements S {
    /** 成功：build 系列方法据此判定 success=true */
    OK("0", "成功"),

    // ---- 一级宏观错误码：无法更具体确定错误场景时的兜底 ----

    /** 用户端错误兜底 */
    CLIENT_ERROR("A0001", "用户端错误"),

    /** 系统执行出错兜底（通用失败） */
    FAIL("B0001", "系统执行出错"),

    /** 第三方服务出错兜底 */
    THIRD_PARTY_ERROR("C0001", "调用第三方服务出错"),

    // ---- A 用户端错误 ----

    /** 登录态缺失（手册 A0200 用户登陆异常，取常用语义） */
    NOT_LOGIN("A0200", "用户未登录"),

    /** 登录已过期 */
    LOGIN_EXPIRED("A0230", "用户登录已过期"),

    /** 访问未授权 */
    NO_PERMISSION("A0301", "访问未授权"),

    /** 用户请求参数错误（二级宏观） */
    ARGUMENT_ILLEGAL("A0400", "用户请求参数错误"),

    /** 请求必填参数为空 */
    ARGUMENT_ABSENCE("A0410", "请求必填参数为空"),

    /** 请求次数超出限制（限流） */
    RATE_LIMIT("A0501", "请求次数超出限制"),

    // ---- B 系统执行出错 ----

    /** 系统执行超时 */
    SERVICE_TIMEOUT("B0100", "系统执行超时"),

    /** 获取分布式锁失败（手册未定义，按 B 类结构自定义扩展） */
    GET_LOCK_ERROR("B0900", "获取分布式锁失败"),

    // ---- C 调用第三方服务出错 ----

    /** 数据库服务出错（二级宏观） */
    DB_ERROR("C0300", "数据库服务出错"),

    /** 主键冲突（数据重复） */
    DATA_DUPLICATE("C0341", "主键冲突"),

    ;

    private final String code;

    private final String msg;
}
