package org.tools.bedrock.util;

import java.util.List;
import java.util.Map;

/**
 * 断言工具：参数校验失败时抛出异常
 *
 * @author sander
 * @since 1.0.6.5
 */
public class Assert {
    private Assert() {
    }

    /**
     * 对象非 null
     *
     * @param obj      对象
     * @param errorMsg 错误信息
     * @param params   参数
     * @param <T>      泛型
     * @return boolean
     * @throws IllegalArgumentException obj 为 null 时抛出
     */
    public static <T> T isNotNull(T obj, String errorMsg, Object... params) throws IllegalArgumentException {
        if (BaseUtils.isNull(obj)) {
            throw new IllegalArgumentException(String.format(errorMsg, params));
        }
        return obj;
    }

    /**
     * 对象非 null
     *
     * @param obj 对象
     * @throws IllegalArgumentException obj 为 null 时抛出（与 {@link #isNotNull(Object, String, Object...)} 一致）
     */
    public static void isNotNull(Object obj) {
        if (BaseUtils.isNull(obj)) {
            throw new IllegalArgumentException("对象不能为空");
        }
    }

    /**
     * List为 空
     *
     * @param list 集合
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Map为 空
     *
     * @param map 集合
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 对象为 空
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().trim().isEmpty();
    }

    /**
     * 对象非 空
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj) {
        return obj != null && !obj.toString().trim().isEmpty();
    }
}
