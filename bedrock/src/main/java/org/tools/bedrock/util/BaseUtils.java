package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class BaseUtils {

    private BaseUtils() {
    }

    /**
     * 对象为 null
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 对象非 null
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
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

    /**
     * 多个对象是否为 null
     * <p>
     * 传入对象数组中存在 null 对象，返回 true 反之 false
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isNull(Object... objs) {
        for (Object obj : objs) {
            if (isNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 多个对象为 null
     * <p>
     * 传入对象数组中存在非 null对象，返回 false 反之 true
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isAllNull(Object... objs) {
        for (Object obj : objs) {
            if (isNotNull(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 多个对象是否非 null
     * <p>
     * 传入对象数组中存在非 null 对象，返回 true 反之 false
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isNotNull(Object... objs) {
        for (Object obj : objs) {
            if (isNull(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 多个对象非 null
     * <p>
     * 传入对象数组中存在 null 对象，返回 false 反之 true
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isAllNotNull(Object... objs) {
        for (Object obj : objs) {
            if (isNull(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 多个对象为 空
     * <p>
     * 传入对象数组中存在 空 对象，返回 true 反之 false
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isEmpty(Object... objs) {
        if (isNull(objs)) {
            return true;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?> && isEmpty((List<?>) obj)) {
                return true;
            }
            if (obj instanceof Map<?, ?> && isEmpty((Map<?, ?>) obj)) {
                return true;
            }
            if (obj instanceof Object[]) {
                if (isEmpty((Object[]) obj)) {
                    return true;
                }
                continue;
            }
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 多个对象为 空
     * <p>
     * 传入对象数组中存在非 空 对象，返回 false 反之 true
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isAllEmpty(Object... objs) {
        if (isAllNull(objs)) {
            return true;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?>) {
                if (isNotEmpty((List<?>) obj)) {
                    return false;
                }
                continue;
            }
            if (obj instanceof Map<?, ?>) {
                if (isNotEmpty((Map<?, ?>) obj)) {
                    return false;
                }
                continue;
            }
            if (obj instanceof Object[]) {
                if (isNotEmpty((Object[]) obj)) {
                    return false;
                }
                continue;
            }
            if (isNotEmpty(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 集合非 空
     *
     * @param list 集合
     * @return boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * Map非 空
     *
     * @param map 集合
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 多个对象非 空
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isNotEmpty(Object... objs) {
        if (isAllNull(objs)) {
            return false;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?>) {
                if (isNotEmpty((List<?>) obj)) {
                    return true;
                }
                continue;
            }
            if (obj instanceof Map<?, ?>) {
                if (isNotEmpty((Map<?, ?>) obj)) {
                    return true;
                }
                continue;
            }
            if (obj instanceof Object[]) {
                if (isNotEmpty((Object[]) obj)) {
                    return true;
                }
                continue;
            }
            if (isNotEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 多个对象非 空
     * <p>
     * 传入对象数组中存在 空 对象，返回 false 反之 true
     * </p>
     *
     * @param objs 对象数组
     * @return boolean
     */
    public static boolean isAllNotEmpty(Object... objs) {
        if (isNull(objs)) {
            return false;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?> && isEmpty((List<?>) obj)) {
                return false;
            }
            if (obj instanceof Map<?, ?> && isEmpty((Map<?, ?>) obj)) {
                return false;
            }
            if (obj instanceof Object[]) {
                if (isEmpty((Object[]) obj)) {
                    return false;
                }
                continue;
            }
            if (isEmpty(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象属性为 空
     * <p>
     * 嵌套try待优化
     * 对象动态get方法优化从ClassUtils获取
     *
     * @param o   泛型对象
     * @param <O> 泛型
     * @return boolean
     */
    public static <O> boolean isEmptyObj(O o) {
        if (isNull(o)) {
            return true;
        }

        // 获取对象类型名称
        String typeName = o.getClass().getTypeName();
        try {
            // 根据类型名称获取类
            Class<?> aClass = Class.forName(typeName);
            // 获取类中所有方法
            Method[] declaredMethods = aClass.getDeclaredMethods();
            // 遍历方法
            for (Method declaredMethod : declaredMethods) {
                // 获取方法名，如果以get开头，并且返回值不为空，则返回false
                if (declaredMethod.getName().startsWith("get") && !isEmpty(declaredMethod.invoke(o))) {
                    return false;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            log.info("对象判空异常", e);
            return false;
        }
        return true;
    }

}
