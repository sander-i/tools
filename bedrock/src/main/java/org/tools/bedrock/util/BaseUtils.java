package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:40
 * @description 基础方法
 * @scope all
 */
@Slf4j
public class BaseUtils {

    private BaseUtils() {
    }

    /**
     * 对象为 null
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 多个对象为 null
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isNull(Object... objs) {
        for (Object obj : objs) {
            if (isNotNull(obj)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象非 null
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 多个对象非 null
     *
     * @param objs Object...
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
     * List为 空
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Map为 空
     *
     * @param map Map<?, ?>
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 对象为 空
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().trim().isEmpty();
    }

    /**
     * 多个对象为 空
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isEmpty(Object... objs) {
        if (isNull(objs)) {
            return true;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?> && !((List<?>) obj).isEmpty()) {
                return false;
            }
            if (obj instanceof Map<?, ?> && !((Map<?, ?>) obj).isEmpty()) {
                return false;
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
     * List非 空
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    /**
     * Map非 空
     *
     * @param map Map<?, ?>
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 对象非 空
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj) {
        return Objects.nonNull(obj) && !obj.toString().trim().isEmpty();
    }

    /**
     * 多个对象非 空
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isNotEmpty(Object... objs) {
        if (isNull(objs)) {
            return false;
        }

        for (Object obj : objs) {
            if (obj instanceof List<?> && ((List<?>) obj).isEmpty()) {
                return false;
            }
            if (obj instanceof Map<?, ?> && ((Map<?, ?>) obj).isEmpty()) {
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
