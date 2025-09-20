package org.tools.fortify.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.bedrock.util.ClassUtils;
import org.tools.bedrock.util.date.DateUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json方法
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class JSONUtils {

    private JSONUtils() {
    }

    /**
     * @param obj 转换对象
     * @return String json字符串
     */
    public static String objToJson(Object obj) {
        return BaseUtils.isNotEmpty(obj) ? JSON.toJSONString(obj) : "";
    }

    /**
     * @param jsonStr json字符串
     * @return JSONObject 转换对象
     */
    public static JSONObject jsonToObj(String jsonStr) {
        return BaseUtils.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr) : new JSONObject();
    }

    /**
     * @param jsonStr json字符串
     * @param clz     转换对象class
     * @return Object 转换对象
     */
    public static Object jsonToObj(String jsonStr, Class<?> clz) {
        return BaseUtils.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr, clz) : new JSONObject();
    }

    /**
     * @param obj 转换对象
     * @param clz class
     * @param <T> 范型
     * @return String json字符串
     */
    public static <T> List<T> objToJsonArray(Object obj, Class<T> clz) {
        return BaseUtils.isNotEmpty(obj) ? JSON.parseArray(objToJson(obj), clz) : new ArrayList<>();
    }

    /**
     * 将JSON字符串转换为指定对象
     *
     * @param jsonStr JSON字符串
     * @param obj     目标对象实例
     * @return 转换后的对象
     * @throws IllegalArgumentException 当JSON字符串无效时抛出异常
     */
    public static Object jsonToObj(String jsonStr, Object obj) {
        // 解析JSON字符串为JSONObject对象
        JSONObject jsonObj = jsonToObj(jsonStr);
        if (jsonObj == null) {
            throw new IllegalArgumentException("Invalid JSON string: " + jsonStr);
        }

        // 获取JSON对象内部的键值对映射
        Map<String, Object> jsonObjInnerMap = jsonObj.getInnerMap();
        if (jsonObjInnerMap == null) {
            return obj;
        }

        // 构建目标对象的字段映射和属性描述符缓存
        Class<?> cls = obj.getClass();
        Map<String, Field> fieldMap = buildFieldMap(cls);
        Map<String, PropertyDescriptor> pdCache = new HashMap<>();

        // 遍历JSON对象的每个键值对，处理对应的字段赋值
        for (Map.Entry<String, Object> jsonEntry : jsonObjInnerMap.entrySet()) {
            processField(jsonEntry, obj, cls, fieldMap, pdCache);
        }

        return obj;
    }

    /**
     * 构建类字段映射表
     *
     * @param cls 需要解析的类对象
     * @return 包含类所有字段的映射表，键为字段名，值为字段对象
     */
    private static Map<String, Field> buildFieldMap(Class<?> cls) {
        Map<String, Field> fieldMap = new HashMap<>();
        // 获取类的所有声明字段
        Field[] fields = ClassUtils.getAllDeclaredFields(cls);
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    /**
     * 处理JSON对象中的单个字段，将其值设置到目标对象的对应字段中
     *
     * @param jsonEntry JSON键值对条目，包含字段名和字段值
     * @param obj       目标对象实例，将被设置字段值
     * @param cls       目标对象的类类型
     * @param fieldMap  字段映射表，用于快速查找字段
     * @param pdCache   属性描述符缓存，避免重复创建PropertyDescriptor对象
     */
    private static void processField(Map.Entry<String, Object> jsonEntry, Object obj,
                                     Class<?> cls, Map<String, Field> fieldMap,
                                     Map<String, PropertyDescriptor> pdCache) {
        String jsonEntryKey = jsonEntry.getKey();
        Object jsonEntryVal = jsonEntry.getValue();

        // 如果JSON值为null或找不到对应字段，则直接返回
        if (jsonEntryVal == null) return;

        Field field = fieldMap.get(jsonEntryKey);
        if (field == null) return;

        String fieldName = field.getName();
        Class<?> fieldTypeClass = field.getType();

        // 从缓存中获取或创建属性描述符
        PropertyDescriptor pd = pdCache.computeIfAbsent(fieldName, k -> {
            try {
                return new PropertyDescriptor(fieldName, cls);
            } catch (IntrospectionException e) {
                throw new PropertyIntrospectionException("Failed to introspect property: " + fieldName, e);
            }
        });

        Method setMethod = pd.getWriteMethod();
        // 如果没有setter方法，则直接返回
        if (setMethod == null) return;

        try {
            // 根据字段类型选择不同的处理方式
            if (isSimpleType(fieldTypeClass)) {
                handleSimpleTypeField(setMethod, obj, jsonEntryVal, field);
            } else {
                handleComplexObjectField(setMethod, obj, jsonEntryVal, fieldTypeClass);
            }
        } catch (Exception e) {
            log.error("Failed to set field: {}, value: {}, error: {}", fieldName, jsonEntryVal, e.getMessage(), e);
        }
    }

    /**
     * 判断给定的类是否为简单类型
     *
     * @param clazz 待判断的类对象
     * @return 如果是简单类型返回true，否则返回false
     */
    private static boolean isSimpleType(Class<?> clazz) {
        // 判断是否为基础类型、java.lang包下的类或java包下的类
        return ClassUtils.isBaseClass(clazz.getSimpleName()) ||
                clazz.getName().startsWith("java.lang") ||
                clazz.getName().contains("java");
    }

    /**
     * 处理简单类型字段的赋值操作
     *
     * @param setMethod    setter方法对象，用于调用字段的设置方法
     * @param obj          目标对象实例，需要设置字段值的对象
     * @param jsonEntryVal JSON数据中的值，从JSON解析得到的字段值
     * @param field        字段对象，表示需要设置值的字段
     * @throws Exception 当方法调用或类型转换过程中发生错误时抛出异常
     */
    private static void handleSimpleTypeField(Method setMethod, Object obj, Object jsonEntryVal,
                                              Field field) throws Exception {
        Class<?> fieldTypeClass = field.getType();
        // 处理日期类型字段
        if (fieldTypeClass == Date.class) {
            handleDateField(setMethod, obj, jsonEntryVal);
            // 处理List类型字段
        } else if (List.class.isAssignableFrom(fieldTypeClass)) {
            handleListField(setMethod, obj, jsonEntryVal, field);
            // 处理Map类型字段，直接赋值
        } else if (Map.class.isAssignableFrom(fieldTypeClass)) {
            setMethod.invoke(obj, jsonEntryVal);
            // 处理StringBuffer类型字段，需要创建新的StringBuffer实例
        } else if (fieldTypeClass == StringBuffer.class) {
            setMethod.invoke(obj, new StringBuffer(jsonEntryVal.toString()));
            // 处理StringBuilder类型字段，需要创建新的StringBuilder实例
        } else if (fieldTypeClass == StringBuilder.class) {
            setMethod.invoke(obj, new StringBuilder(jsonEntryVal.toString()));
            // 处理其他简单类型字段，直接赋值
        } else {
            setMethod.invoke(obj, jsonEntryVal);
        }
    }

    /**
     * 处理日期类型的字段赋值
     *
     * @param setMethod    setter方法对象，用于调用目标对象的设值方法
     * @param obj          目标对象，需要被设置日期字段值的对象实例
     * @param jsonEntryVal JSON数据中的值，可能是Long类型的时间戳或字符串格式的日期
     * @throws Exception 当方法调用或日期解析过程中发生错误时抛出异常
     */
    private static void handleDateField(Method setMethod, Object obj, Object jsonEntryVal) throws Exception {
        // 如果JSON值是Long类型，直接作为时间戳创建Date对象
        if (jsonEntryVal instanceof Long) {
            setMethod.invoke(obj, new Date((Long) jsonEntryVal));
        } else {
            // 尝试将非Long类型的值解析为日期格式
            try {
                Date date = DateUtils.strDateFormat(jsonEntryVal.toString());
                setMethod.invoke(obj, date);
            } catch (Exception e) {
                log.warn("Failed to parse date from value: {}", jsonEntryVal, e);
            }
        }
    }

    /**
     * 处理List类型的字段赋值操作
     *
     * @param setMethod    setter方法对象，用于调用设置字段值的方法
     * @param obj          目标对象实例，需要设置List字段值的对象
     * @param jsonEntryVal JSON数据中对应字段的值
     * @param field        需要处理的字段对象
     * @throws Exception 当方法调用或对象创建过程中发生异常时抛出
     */
    private static void handleListField(Method setMethod, Object obj, Object jsonEntryVal,
                                        Field field) throws Exception {
        // 获取字段的泛型类型信息
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            setMethod.invoke(obj, jsonEntryVal);
            return;
        }

        // 获取List的泛型参数类型
        Type actualType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        if (!(actualType instanceof Class<?>)) {
            setMethod.invoke(obj, jsonEntryVal);
            return;
        }

        // 处理List中的每个元素，根据元素类型进行不同的转换处理
        Class<?> elementClass = (Class<?>) actualType;
        List<Object> fieldList = new ArrayList<>();
        if (jsonEntryVal instanceof List<?>) {
            for (Object item : (List<?>) jsonEntryVal) {
                // 如果是基础类型或java.lang包下的类，直接添加到列表中
                if (ClassUtils.isBaseClass(elementClass.getSimpleName()) ||
                        ClassUtils.isJavaLangType(elementClass)) {
                    fieldList.add(item);
                } else {
                    // 如果是自定义对象类型，需要进行JSON序列化和反序列化转换
                    try {
                        Object newInstance = elementClass.getDeclaredConstructor().newInstance();
                        String itemJson = objToJson(item);
                        jsonToObj(itemJson, newInstance);
                        fieldList.add(newInstance);
                    } catch (Exception ex) {
                        log.warn("Failed to convert list item to object: {}", item, ex);
                    }
                }
            }
        }
        setMethod.invoke(obj, fieldList);
    }

    /**
     * 处理复杂对象字段的赋值操作
     *
     * @param setMethod      setter方法对象，用于调用目标对象的设值方法
     * @param obj            目标对象实例，需要被设置复杂对象字段的对象
     * @param jsonEntryVal   JSON数据中的字段值，用于构建复杂对象
     * @param fieldTypeClass 复杂对象字段的类型类，用于创建新的实例
     */
    private static void handleComplexObjectField(Method setMethod, Object obj, Object jsonEntryVal,
                                                 Class<?> fieldTypeClass) {
        try {
            // 创建复杂对象的实例
            Object fieldInstance = ClassUtils.newInstance(fieldTypeClass.getName());
            // 将JSON值转换为JSON字符串
            String jsonString = objToJson(jsonEntryVal);
            // 将JSON字符串解析并填充到复杂对象实例中
            jsonToObj(jsonString, fieldInstance);
            // 调用setter方法将复杂对象实例设置到目标对象中
            setMethod.invoke(obj, fieldInstance);
        } catch (Exception e) {
            log.warn("Failed to instantiate complex object for field type: {}", fieldTypeClass.getName(), e);
        }
    }

}
