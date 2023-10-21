package org.tools.bedrock.util;

import lombok.extern.log4j.Log4j2;
import org.tools.bedrock.constant.Constants;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:40
 * @description 基础方法
 * @scope all
 */
@Log4j2
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
        return !isNull(obj);
    }

    /**
     * 多个对象非 null
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isNotNull(Object... objs) {
        return !isNull(objs);
    }

    /**
     * List为 空
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return isNull(list) || list.isEmpty();
    }

    /**
     * Map为 空
     *
     * @param map Map<?, ?>
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * 对象为 空
     *
     * @param obj Object
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return isNull(obj) || obj.toString().trim().length() == 0;
    }

    /**
     * 多个对象为 空
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isEmpty(Object... objs) {
        if (isNull(objs)) return true;

        for (Object obj : objs) {
            if (obj instanceof List<?> && (!isEmpty((List<?>) obj))) {
                return false;
            }
            if (obj instanceof Map<?, ?> && (!isEmpty((Map<?, ?>) obj))) {
                return false;
            }
            if (obj instanceof Object[] && (!isEmpty((Object[]) obj))) {
                return false;
            }
            if (!isEmpty(obj)) {
                return false;
            }
        }
        return objs.length == 0;
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
        return !isEmpty(obj);
    }

    /**
     * 多个对象非 空
     *
     * @param objs Object...
     * @return boolean
     */
    public static boolean isNotEmpty(Object... objs) {
        if (isNull(objs)) return false;

        for (Object obj : objs) {
            if (obj instanceof List<?> && (isEmpty((List<?>) obj))) {
                return false;
            }
            if (obj instanceof Map<?, ?> && (isEmpty((Map<?, ?>) obj))) {
                return false;
            }
            if (obj instanceof Object[] && (isEmpty((Object[]) obj))) {
                return false;
            }
            if (isEmpty(obj)) {
                return false;
            }
        }
        return objs.length != 0;
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
        if (isNull(o)) return true;

        String typeName = o.getClass().getTypeName();
        Class<?> aClass;
        Method[] declaredMethods;
        try {
            aClass = Class.forName(typeName);
            declaredMethods = aClass.getDeclaredMethods();
            Object invoke;
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().indexOf("get") == 0) {
                    invoke = declaredMethod.invoke(o);
                    if (!isEmpty(invoke)) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            log.info("对象判空异常", e);
            return false;
        }
        return true;
    }

    /**
     * List 多条记录
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean multiRecord(List<?> list) {
        return list.size() > Constants.ONE_I;
    }

    /**
     * List 记录是否满足传入条数
     *
     * @param list List<?>
     * @param size 大小
     * @return boolean
     */
    public static boolean otherRecord(List<?> list, int size) {
        return list.size() == size;
    }

    /**
     * List 一条记录
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean oneRecord(List<?> list) {
        return otherRecord(list, Constants.ONE_I);
    }

}
