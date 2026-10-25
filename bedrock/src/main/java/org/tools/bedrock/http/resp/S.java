package org.tools.bedrock.http.resp;

/**
 * 响应状态定义契约。
 *
 * <p>统一响应体状态码与提示文本的抽象接口：业务方可通过实现本接口定义
 * 自有状态集（如 {@link RS}），供 {@link RR}/{@link R} 的 {@code build}
 * 工厂方法消费，避免与具体枚举耦合。</p>
 *
 * @author sander 2024/02/12
 * @version 2.0
 */
public interface S {

    String getCode();

    String getMsg();

}
