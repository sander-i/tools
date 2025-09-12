package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class Maps {

    private Maps() {
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
     * Map非 空
     *
     * @param map 集合
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
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
    @SuppressWarnings("unchecked")
    public static <T> T mapToObj(Map<String, Object> sourceMap, T target) {
        if (BaseUtils.isEmpty(sourceMap) || target == null) {
            return target;
        }

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
        if (BaseUtils.isEmpty(sourceMap) || target == null) {
            return null;
        }

        try {
            return mapToObj(sourceMap, target.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
        if (BaseUtils.isEmpty(sourceMap) || target == null) {
            return target;
        }

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
        if (BaseUtils.isEmpty(sourceMap) || target == null) {
            return target;
        }

        Class<?> clz = target.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性（保留原逻辑）
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        // 构建字段名到 Field 的映射，提高查找效率
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : declaredFields) {
            fieldMap.put(field.getName(), field);
        }

        for (Map.Entry<String, Object> source : sourceMap.entrySet()) {
            String sourceKey = source.getKey();
            String targetName = relations.get(sourceKey);

            if (targetName == null) {
                continue;
            }

            Field targetField = fieldMap.get(targetName);
            if (targetField == null) {
                continue;
            }

            // 如果 precedenceRelation 为 true，则跳过已有值的属性（不覆盖已有的值）
            if (precedenceRelation && ClassUtils.hasPropertyValue(target, targetName)) {
                continue;
            }

            Object entityMapValue = source.getValue();
            try {
                ClassUtils.setProperty(target, targetName, entityMapValue);
            } catch (IllegalArgumentException e) {
                log.error("设置属性值失败 - 类型不匹配，字段: {}, 值: {}, 错误: {}", targetName, entityMapValue, e.getMessage(), e);
            } catch (Exception e) {
                log.error("设置属性值失败 - 其他异常，字段: {}, 错误: {}", targetName, e.getMessage(), e);
            }
        }

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
        if (BaseUtils.isEmpty(sourceMap) || target == null) {
            return null;
        }

        try {
            return mapToObj(sourceMap, relations, target.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 合并Map
     *
     * @param source 源
     * @param target 目标
     * @param <T>    T
     * @param <O>    O
     * @return 合并后Map
     */
    public static <T, O> Map<T, List<O>> mergeMap(Map<T, List<O>> source, Map<T, List<O>> target) {
        // 处理null情况
        if (source == null && target == null) {
            return Collections.emptyMap();
        }

        if (source == null) {
            return Collections.emptyMap();
        }

        if (target == null) {
            return Collections.emptyMap();
        }

        // 合并逻辑
        target.forEach((k, v) -> {
            final List<O> sourceValues = source.get(k);
            if (sourceValues == null) {
                // 如果source中没有这个key，直接添加（确保创建可修改的列表）
                source.put(k, v == null ? new ArrayList<>() : new ArrayList<>(v));
            } else if (v != null) {
                // 如果source中有这个key且target的值不为null，合并列表
                // 创建新的可修改列表以避免UnsupportedOperationException
                List<O> mergedList = new ArrayList<>(sourceValues);
                mergedList.addAll(v);
                source.put(k, mergedList);
            }
            // 如果v为null，但source中已存在该key，则不改变source中的值
        });

        return source;
    }

    /**
     * 合并Map
     *
     * @param source  源
     * @param targets 目标
     * @param <T>     T
     * @param <O>     O
     * @return 合并后Map
     */
    @SafeVarargs
    public static <T, O> Map<T, List<O>> mergeMap(Map<T, List<O>> source, Map<T, List<O>>... targets) {
        if (source == null) {
            return null;
        }

        if (targets == null) {
            return source;
        }

        for (Map<T, List<O>> target : targets) {
            if (target != null) {
                mergeMap(source, target);
            }
        }

        return source;
    }

}
