package org.tools.bedrock.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xietao
 * @version 1.0
 * @date 2023/12/31 19:58
 * @description 描述
 * @scope 作用域
 */
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
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        if (BaseUtils.isEmpty(obj)) {
            return Collections.emptyMap();
        }
        Class<?> clz = obj.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        Map<String, Object> map = new HashMap<>();

        for (Field oldField : declaredFields) {
            String fieldName = ClassUtils.handleBooleanType(oldField);
            String fieldType = oldField.getType().getName();

            /* java基础类 */
            Object value = ClassUtils.getProperty(obj, fieldName);
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
     * @param entityMap
     * @param relationship
     * @param obj
     * @return
     */
    public static Object mapToObj(Map<String, Object> entityMap, Map<String, String> relationship, Object obj) {
        Class<?> clz = obj.getClass();
        // 获取所有属性
        Field[] declaredFields = ClassUtils.getAllDeclaredFields(clz);
        // 去除方法未找到的属性
        declaredFields = ClassUtils.roverMethodNotFoundDeclaredFields(declaredFields, clz);

        for (Map.Entry<String, Object> entity : entityMap.entrySet()) {
            String entityMapKey = entity.getKey();

            for (Map.Entry<String, String> relationEntity : relationship.entrySet()) {

                String originalName = relationEntity.getKey();
                String corresponName = relationEntity.getValue();

                if (entityMapKey.equals(corresponName)) {

                    for (Field oldField : declaredFields) {

                        String fieldName = oldField.getName();

                        if (originalName.equals(fieldName)) {

                            Object entityMapValue = entity.getValue();

                            ClassUtils.setProperty(obj, fieldName, entityMapValue);
                        }
                    }
                }
            }
        }
        return obj;
    }

}
