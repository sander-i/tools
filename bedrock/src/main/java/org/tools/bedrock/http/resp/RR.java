package org.tools.bedrock.http.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.tools.bedrock.annotations.Inherit;

import java.io.Serializable;

/**
 * 通用响应对象（无数据）。
 *
 * <p>HTTP 统一响应体的基础载体，字段语义：
 * {@code success} 成功标记、{@code code} 业务状态码、
 * {@code message} 提示信息（状态定义的标准提示与调用方自定义提示合并后的最终文案）、
 * {@code timestamp} 服务端响应时间戳（epoch 毫秒，构造时自动初始化）、
 * {@code traceId} 链路追踪 ID（可空，框架未自动注入时由调用方通过 setter 设置）。
 * 需要携带业务数据时使用子类 {@link R}。</p>
 *
 * <p>字段图整体实现 {@link Serializable}，可在 RPC、缓存等 JDK 序列化场景使用；
 * 字段均为 Jackson 可直接序列化的基础类型，符合 JSON 响应体约定。</p>
 *
 * @author sander 2024/08/03
 * @version 2.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inherit
public class RR implements Serializable {
    private static final long serialVersionUID = 2604336207335005109L;

    private boolean success;

    private String code;

    private String message;

    /**
     * 服务端响应时间戳（epoch 毫秒）。
     * 构造时自动初始化为当前时间；JDK 反序列化旧版本流时为 0，调用方需自行判断。
     */
    private long timestamp = System.currentTimeMillis();

    /** 链路追踪 ID，可空；由调用方结合自身框架（如 MDC）按需设置 */
    private String traceId;

    protected RR(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    /**
     * 构建无数据的成功响应
     *
     * @return 成功响应对象
     */
    public static RR ok() {
        return new RR(true, RS.OK.getCode(), RS.OK.getMsg());
    }

    /**
     * 构建无数据的失败响应（success=false）
     *
     * @return 失败响应对象
     */
    public static RR fail() {
        return new RR(false, RS.FAIL.getCode(), RS.FAIL.getMsg());
    }

    /**
     * 按状态枚举构建 RR 响应：code 匹配 {@link RS#OK} 时 success=true，否则 false。
     *
     * @param s 状态定义
     * @return RR 响应
     */
    public static RR build(S s) {
        return build(s, RS.OK.getCode().equals(s.getCode()));
    }

    /**
     * 按状态枚举与显式的 success 标记构建 RR 响应。
     *
     * @param s       状态定义
     * @param success 是否成功
     * @return RR 响应
     */
    public static RR build(S s, boolean success) {
        return new RR(success, s.getCode(), s.getMsg());
    }
}
