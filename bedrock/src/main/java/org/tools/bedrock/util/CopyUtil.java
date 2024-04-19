package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author sander
 * @version 1.0
 * @date 2023/12/31 11:26
 * @description 描述
 * @scope 作用域
 */
@Slf4j
public class CopyUtil {

    private CopyUtil() {
    }

    /**
     * 复制对象
     *
     * @param source           源对象
     * @param target           目标对象
     * @param current          当前对象 Recommend false
     *                         <p>
     *                         注意：如果源对象或目标对象存在父类属性，仅复制当前对象目标对象可能会出现部分属性没有参数
     *                         </p>
     *                         <p>
     *                         如果：当前对象为false时，会复制父类属性
     *                         </p>
     *                         <p>
     *                         否则：当前对象为true时，不会复制父类属性
     *                         </p>
     *                         <p>
     *                         因此：不确定是否存在父类属性的情况下，建议传true
     *                         </p>
     * @param typeSame         类型相同 Recommend false
     *                         <p>
     *                         注意：如果字段名相同但类型不同，可能会出现字段复制失败
     *                         </p>
     *                         <p>
     *                         但是：如果开启了链式调用[@Accessors(chain = true)]相同的字段名，相同的类型，也有可能会出现复制失败，加了链式调用后无法获取get|set方法
     *                         </p>
     *                         <p>
     *                         因此：如果需要使用该方法进行跨对象属性复制，操作的对象不建议开启链式调用
     *                         </p>
     * @param ignoreProperties 忽略属性
     * @param <S>              源对象泛型
     * @param <T>              目标对象泛型
     */
    public static <S, T> void copyObj(S source, T target, boolean current, boolean typeSame, String... ignoreProperties) {
        // 获取class对象
        Class<?> sourceClz = source.getClass();
        // 获取class对象
        Class<?> targetClz = target.getClass();
        // 获取两个对象中的共有字段(非共有字段无法进行关联)
        Field[] commonFields = current ?
                ClassUtils.commonFields(sourceClz, targetClz, typeSame) :
                ClassUtils.allCommonFields(sourceClz, targetClz, typeSame);
        // 忽略属性集合
        Set<String> ignoreSet = BaseUtils.isEmpty(ignoreProperties) ?
                Collections.emptySet() : new HashSet<>(Arrays.asList(ignoreProperties));
        // 过滤忽略属性
        List<Field> commonFieldList = Arrays.stream(commonFields)
                .filter(commonField -> {
                    AtomicBoolean isIgnore = new AtomicBoolean(true);
                    ignoreSet.forEach(ignore -> isIgnore.set(!ignore.equals(commonField.getName())));
                    return isIgnore.get();
                }).collect(Collectors.toList());

        for (Field field : commonFieldList) {
            try {
                // 获取源对象处理布尔类型后的属性值
                String fieldName = ClassUtils.handleBooleanType(field);
                Object sourceValue = ClassUtils.getProperty(source, fieldName);
                if (BaseUtils.isEmpty(sourceValue)) {
                    continue;
                }
                // 非java原生对象
                if (!(sourceValue instanceof Comparable)) {
                    // 获目标对象的属性值
                    Object targetValue = ClassUtils.getProperty(target, fieldName);
                    // 如果targetValue为空，则根据field的类型创建一个新的实例
                    if (BaseUtils.isEmpty(targetValue)) {
                        Class<?> clz = field.getType();
                        targetValue = clz.newInstance();
                    }
                    // 递归调用
                    copyObj(sourceValue, targetValue, current, typeSame, ignoreProperties);
                    sourceValue = targetValue;
                }
                // 设置属性值
                ClassUtils.setProperty(target, fieldName, sourceValue);
            } catch (IllegalAccessException | InstantiationException e) {
                log.info(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源对象泛型
     * @param <T>    目标对象泛型
     */
    public static <S, T> void copyObj(S source, T target) {
        copyObj(source, target, false, false);
    }

    /**
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略属性
     * @param <S>              源对象泛型
     * @param <T>              目标对象泛型
     */
    public static <S, T> void copyObj(S source, T target, String... ignoreProperties) {
        copyObj(source, target, false, false, ignoreProperties);
    }

    /**
     * @param source 源对象
     * @param clz    目标对象类信息
     * @param <S>    源对象泛型
     * @param <T>    目标对象泛型
     * @return 目标对象
     */
    public static <S, T> T copyObj(S source, Class<T> clz) {
        T target;
        try {
            // 使用反射创建对象
            target = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        // 将源对象属性值复制到目标对象
        copyObj(source, target, false, false);
        return target;
    }

    /**
     * @param source           源对象
     * @param clz              目标对象类信息
     * @param ignoreProperties 忽略属性
     * @param <S>              源对象泛型
     * @param <T>              目标对象泛型
     * @return 目标对象
     */
    public static <S, T> T copyObj(S source, Class<T> clz, String... ignoreProperties) {
        T target;
        try {
            // 使用反射创建对象
            target = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        // 将源对象属性值复制到目标对象
        copyObj(source, target, false, false, ignoreProperties);
        return target;
    }

    /**
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源对象泛型
     * @param <T>    目标对象泛型
     */
    public static <S, T> void copyCurrentObj(S source, T target) {
        copyObj(source, target, true, false);
    }

    /**
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 忽略属性
     * @param <S>              源对象泛型
     * @param <T>              目标对象泛型
     */
    public static <S, T> void copyCurrentObj(S source, T target, String... ignoreProperties) {
        copyObj(source, target, true, false, ignoreProperties);
    }

    /**
     * @param source 源对象
     * @param clz    目标对象类信息
     * @param <S>    源对象泛型
     * @param <T>    目标对象泛型
     * @return 目标对象
     */
    public static <S, T> T copyCurrentObj(S source, Class<T> clz) {
        T target;
        try {
            //使用反射创建对象
            target = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        //将源对象属性值复制到目标对象
        copyObj(source, target, true, false);
        return target;
    }

    /**
     * @param source           源对象
     * @param clz              目标对象类信息
     * @param ignoreProperties 忽略属性
     * @param <S>              源对象泛型
     * @param <T>              目标对象泛型
     * @return 目标对象
     */
    public static <S, T> T copyCurrentObj(S source, Class<T> clz, String... ignoreProperties) {
        T target;
        try {
            //使用反射创建对象
            target = clz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        //将源对象属性值复制到目标对象
        copyObj(source, target, true, false, ignoreProperties);
        return target;
    }

}
