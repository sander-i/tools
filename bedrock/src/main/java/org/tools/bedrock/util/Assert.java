package org.tools.bedrock.util;


import org.tools.bedrock.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * @author sander
 * @date 2024/11/02
 **/
public class Assert {

    private Assert() {
    }

    /**
     * 对象为 null
     *
     * @param obj 对象
     */
    public static <T> T isNotNull(T obj, String errorMsg, Object... params) throws IllegalArgumentException {
        if (BaseUtils.isNotNull(obj)) {
            throw new IllegalArgumentException(String.format(errorMsg, params));
        }
        return obj;
    }

    /**
     * 对象非 null
     *
     * @param obj 对象
     */
    public static void isNotNull(Object obj) {
        if (BaseUtils.isNull(obj)) {
            throw new BusinessException();
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
