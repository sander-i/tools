package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应对象（携带数据）。
 *
 * <p>泛型约束为 {@code T extends Serializable}：在编译期保障 {@code data} 字段
 * 参与 JDK 序列化时不会因顶层类型不可序列化而抛 {@link java.io.NotSerializableException}。
 * 注意该约束的边界：只能校验顶层类型声明，若 T 的非 transient 字段图中含不可序列化对象，
 * 运行期仍会失败——约定 DTO 及其字段图整体实现 {@link Serializable}。</p>
 *
 * <p>无数据场景使用父类 {@link RR}。</p>
 *
 * @author sander
 * @version 2.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class R<T extends Serializable> extends RR {

    private static final long serialVersionUID = -3650942902842307882L;

    /**
     * 响应数据：类型约束 {@code T extends Serializable} 在编译期保障 JDK 序列化可用；
     * T 的字段图需整体可序列化（顶层校验无法覆盖非 transient 嵌套字段）。
     */
    private T data;

    private R(boolean success, S s, String message, T data) {
        super(success, s.getCode(), message != null ? message : s.getMsg());
        this.data = data;
    }

    /** 构建无数据的成功响应（RR 视图） */
    public static RR ok() {
        return RR.ok();
    }

    /** 构建无数据的失败响应（RR 视图） */
    public static RR fail() {
        return RR.fail();
    }

    /** 构建携带数据的成功响应 */
    public static <T extends Serializable> R<T> ok(T data) {
        return new R<>(true, RS.OK, null, data);
    }

    /** 构建携带错误信息的失败响应 */
    public static <T extends Serializable> R<T> fail(String message) {
        return new R<>(false, RS.FAIL, message, null);
    }

    /** 构建携带数据的失败响应 */
    public static <T extends Serializable> R<T> fail(T data) {
        return new R<>(false, RS.FAIL, null, data);
    }

    /** 按状态枚举与消息构建 RR 响应（code 匹配 RS.OK 时 success=true；message 为空时回退状态定义的标准提示） */
    public static RR build(S s, String message) {
        return new RR(RS.OK.getCode().equals(s.getCode()), s.getCode(), message != null ? message : s.getMsg());
    }

    /** 按状态枚举与数据构建响应（code 匹配 RS.OK 时 success=true） */
    public static <T extends Serializable> R<T> build(S s, T data) {
        return new R<>(RS.OK.getCode().equals(s.getCode()), s, null, data);
    }

    /** 按状态枚举、消息与数据构建响应（code 匹配 RS.OK 时 success=true） */
    public static <T extends Serializable> R<T> build(S s, String message, T data) {
        return new R<>(RS.OK.getCode().equals(s.getCode()), s, message, data);
    }

}
