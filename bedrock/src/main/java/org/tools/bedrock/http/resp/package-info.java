/**
 * HTTP 统一响应体（envelope）对象集。
 *
 * <p>由四类角色组成：{@link org.tools.bedrock.http.resp.S} 状态定义契约、
 * {@link org.tools.bedrock.http.resp.RS} 默认状态枚举、
 * {@link org.tools.bedrock.http.resp.RR} 无数据响应对象及其子类
 * {@link org.tools.bedrock.http.resp.R} 携带数据的响应对象。
 * 服务端 Controller 以 {@code R}/{@code RR} 作为返回值包装业务结果，
 * 客户端按 {@code success}/{@code code} 判定结果，按 {@code data} 取业务数据。</p>
 *
 * @author sander
 * @since 1.0.4
 */
package org.tools.bedrock.http.resp;
