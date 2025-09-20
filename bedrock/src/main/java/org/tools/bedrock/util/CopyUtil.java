package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sander
 * @version 1.0
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
        Set<String> ignoreSet = BaseUtils.isNotEmpty((Object[]) ignoreProperties) ?
                new HashSet<>(Arrays.asList(ignoreProperties)) : Collections.emptySet();
        // 过滤忽略属性
        List<Field> commonFieldList = Arrays.stream(commonFields)
                .filter(commonField -> !ignoreSet.contains(commonField.getName()))
                .collect(Collectors.toList());

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
                        targetValue = createInstance(field.getType());
                    }
                    // 处理集合和普通对象
                    sourceValue = handleComplexObject(sourceValue, targetValue, current, typeSame, ignoreProperties);
                }
                // 设置属性值
                ClassUtils.setProperty(target, fieldName, sourceValue);
            } catch (Exception e) {
                log.info(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * 根据类型创建实例
     *
     * @param clz 类型
     * @return 实例
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static Object createInstance(Class<?> clz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (List.class.isAssignableFrom(clz)) {
            return new java.util.ArrayList<>();
        } else if (Map.class.isAssignableFrom(clz)) {
            return new java.util.HashMap<>();
        } else if (Set.class.isAssignableFrom(clz)) {
            return new java.util.HashSet<>();
        } else {
            return clz.getDeclaredConstructor().newInstance();
        }
    }

    /**
     * 处理复杂对象（List、Map、Set、普通对象）
     *
     * @param sourceValue      源值
     * @param targetValue      目标值
     * @param current          是否只处理当前类
     * @param typeSame         是否要求类型相同
     * @param ignoreProperties 忽略属性
     * @return 处理后的值
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static Object handleComplexObject(Object sourceValue, Object targetValue, boolean current, boolean typeSame, String... ignoreProperties)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 如果是List类型，需要特殊处理
        if (sourceValue instanceof List && targetValue instanceof List) {
            return handleList((List<?>) sourceValue, (List<Object>) targetValue, current, typeSame, ignoreProperties);
        }
        // 如果是Map类型，需要特殊处理
        else if (sourceValue instanceof Map && targetValue instanceof Map) {
            return handleMap((Map<?, ?>) sourceValue, (Map<Object, Object>) targetValue, current, typeSame, ignoreProperties);
        }
        // 如果是Set类型，需要特殊处理
        else if (sourceValue instanceof Set && targetValue instanceof Set) {
            return handleSet((Set<?>) sourceValue, (Set<Object>) targetValue, current, typeSame, ignoreProperties);
        } else {
            // 递归调用
            copyObj(sourceValue, targetValue, current, typeSame, ignoreProperties);
            return targetValue;
        }
    }

    /**
     * 处理List类型
     *
     * @param sourceList       源List
     * @param targetList       目标List
     * @param current          是否只处理当前类
     * @param typeSame         是否要求类型相同
     * @param ignoreProperties 忽略属性
     * @return 处理后的List
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static List<Object> handleList(List<?> sourceList, List<Object> targetList, boolean current, boolean typeSame, String... ignoreProperties)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Object item : sourceList) {
            targetList.add(copyOrNewInstance(item, current, typeSame, ignoreProperties));
        }
        return targetList;
    }

    /**
     * 处理Map类型
     *
     * @param sourceMap        源Map
     * @param targetMap        目标Map
     * @param current          是否只处理当前类
     * @param typeSame         是否要求类型相同
     * @param ignoreProperties 忽略属性
     * @return 处理后的Map
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static Map<Object, Object> handleMap(Map<?, ?> sourceMap, Map<Object, Object> targetMap, boolean current, boolean typeSame, String... ignoreProperties)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Map.Entry<?, ?> entry : sourceMap.entrySet()) {
            Object key = entry.getKey();
            Object value = copyOrNewInstance(entry.getValue(), current, typeSame, ignoreProperties);
            targetMap.put(key, value);
        }
        return targetMap;
    }

    /**
     * 处理Set类型
     *
     * @param sourceSet        源Set
     * @param targetSet        目标Set
     * @param current          是否只处理当前类
     * @param typeSame         是否要求类型相同
     * @param ignoreProperties 忽略属性
     * @return 处理后的Set
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static Set<Object> handleSet(Set<?> sourceSet, Set<Object> targetSet, boolean current, boolean typeSame, String... ignoreProperties)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Object item : sourceSet) {
            targetSet.add(copyOrNewInstance(item, current, typeSame, ignoreProperties));
        }
        return targetSet;
    }

    /**
     * 根据对象类型决定是直接返回还是创建新实例并复制属性
     *
     * @param item             对象
     * @param current          是否只处理当前类
     * @param typeSame         是否要求类型相同
     * @param ignoreProperties 忽略属性
     * @return 处理后的对象
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 非法访问异常
     */
    private static Object copyOrNewInstance(Object item, boolean current, boolean typeSame, String... ignoreProperties)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (item == null) {
            return null;
        }
        // java原生对象
        if (item instanceof Comparable) {
            return item;
        } else {
            Class<?> itemClass = item.getClass();
            Object newItem = itemClass.getDeclaredConstructor().newInstance();
            copyObj(item, newItem, current, typeSame, ignoreProperties);
            return newItem;
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
            target = clz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
            target = clz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
            target = clz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
            target = clz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        //将源对象属性值复制到目标对象
        copyObj(source, target, true, false, ignoreProperties);
        return target;
    }

}
