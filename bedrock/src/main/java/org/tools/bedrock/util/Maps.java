package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sander
 * @version 1.0
 * @date 2023/12/31 19:58
 * @description 描述
 * @scope 作用域
 */
@Slf4j
public class Maps {

    private Maps() {
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
     * Map非 空
     *
     * @param map Map<?, ?>
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 对象转Map集合
     *
     * @param source 源对象
     * @param <S>    源对象泛型
     * @return map
     */
    public static <S> Map<String, Object> objToMap(S source) {
        if (BaseUtils.isEmpty(source)) {
            return Collections.emptyMap();
        }
        Class<?> clz = source.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        Map<String, Object> map = new HashMap<>();

        for (Field oldField : declaredFields) {
            String fieldName = ClassUtils.handleBooleanType(oldField);
            String fieldType = oldField.getType().getName();

            /* java基础类 */
            Object value = ClassUtils.getProperty(source, fieldName);
            /* 非java基础类 */
            if (BaseUtils.isNotEmpty(value) && !(fieldType.contains("java") || ClassUtils.isBaseClass(fieldType) || value instanceof Comparable)) {
                value = objToMap(value);
            }
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * Map转对象
     *
     * @param sourceMap 源
     * @param target    目标
     * @param <T>       目标对象泛型
     * @return 目标
     */
    public static <T> T mapToObj(Map<String, Object> sourceMap, T target) {
        Class<?> clz = target.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(ClassUtils.getAllDeclaredFields(clz), clz);

        Set<Map.Entry<String, Object>> sources = sourceMap.entrySet();
        sources = sources.stream().filter(source -> BaseUtils.isNotEmpty(source.getValue())).collect(Collectors.toSet());

        sources.forEach(source -> {
            String sourceKey = source.getKey();
            for (Field oldField : declaredFields) {
                String fieldName = oldField.getName();

                if (!sourceKey.equals(fieldName)) {
                    continue;
                }
                String simpleName = oldField.getType().getSimpleName();

                Object entityMapValue = source.getValue();
                if (!ClassUtils.isBaseClass(simpleName)) {
                    Object o = ClassUtils.newInstance(oldField.getType());
                    if (entityMapValue instanceof Map && BaseUtils.isNotEmpty(o)) {
                        ClassUtils.setProperty(target, fieldName, mapToObj((Map<String, Object>) entityMapValue, o));
                        continue;
                    }
                }
                ClassUtils.setProperty(target, fieldName, entityMapValue);
            }
        });
        return target;
    }

    /**
     * Map转对象
     *
     * @param sourceMap 源
     * @param target    目标
     * @param <T>       目标对象泛型
     * @return 目标
     */
    public static <T> T mapToObj(Map<String, Object> sourceMap, Class<T> target) {
        try {
            return mapToObj(sourceMap, target.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * Map转对象
     *
     * @param sourceMap 源
     * @param relations 关系
     * @param target    目标
     * @param <T>       目标对象泛型
     * @return 目标
     */
    public static <T> T mapToObj(Map<String, Object> sourceMap, Map<String, String> relations, T target) {
        Class<?> clz = target.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        for (Map.Entry<String, Object> source : sourceMap.entrySet()) {
            String sourceKey = source.getKey();

            for (Map.Entry<String, String> relation : relations.entrySet()) {

                String sourceName = relation.getKey();
                String targetName = relation.getValue();

                if (sourceKey.equals(sourceName)) {

                    for (Field oldField : declaredFields) {

                        String fieldName = oldField.getName();

                        if (targetName.equals(fieldName)) {

                            Object entityMapValue = source.getValue();

                            ClassUtils.setProperty(target, fieldName, entityMapValue);
                        }
                    }
                }
            }
        }
        return target;
    }

    /**
     * Map转对象
     *
     * @param sourceMap          源
     * @param relations          关系
     * @param target             目标
     * @param precedenceRelation 优先关系
     * @param <T>                目标对象泛型
     * @return 目标
     */
    public static <T> T mapToObj(Map<String, Object> sourceMap, Map<String, String> relations, T target, boolean precedenceRelation) {
        Class<?> clz = target.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        for (Map.Entry<String, Object> source : sourceMap.entrySet()) {
            String sourceKey = source.getKey();

            for (Map.Entry<String, String> relation : relations.entrySet()) {

                String sourceName = relation.getKey();
                String targetName = relation.getValue();

                if (sourceKey.equals(sourceName)) {

                    for (Field oldField : declaredFields) {

                        String fieldName = oldField.getName();

                        if (targetName.equals(fieldName)) {

                            Object entityMapValue = source.getValue();

                            ClassUtils.setProperty(target, fieldName, entityMapValue);
                        }
                    }
                }
            }
        }

        // todo 过滤 relationship 中不存在的属性 list1
        if (precedenceRelation) {
            // todo 过滤 list1 中 target 目标对象有值的属性（已经通过上面的relationship关系映射赋值了，不能再次覆盖）
            return target;
        }
        // todo 覆盖 list1 中 target 目标对象有值的属性
        return target;
    }

    /**
     * Map转对象
     *
     * @param sourceMap 源
     * @param relations 关系
     * @param target    目标
     * @param <T>       目标对象泛型
     * @return 目标
     */
    public static <T> T mapToObj(Map<String, Object> sourceMap, Map<String, String> relations, Class<T> target) {
        try {
            return mapToObj(sourceMap, relations, target.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

}
