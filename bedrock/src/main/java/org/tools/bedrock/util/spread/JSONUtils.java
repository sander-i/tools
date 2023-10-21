package org.tools.bedrock.util.spread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.tools.bedrock.util.BaseUtils;
import org.tools.bedrock.util.ClassUtils;
import org.tools.bedrock.util.DateUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 13:24
 * @description json方法
 * @scope all
 */
public class JSONUtils {

    private JSONUtils() {
    }

    /**
     * @param obj 转换对象
     * @return String json字符串
     * @description 对象 转 json
     */
    public static String objToJson(Object obj) {
        return BaseUtils.isNotEmpty(obj) ? JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue.getMask()) : "";
    }

    /**
     * @param jsonStr json字符串
     * @return JSONObject 转换对象
     * @description json 转 JSONObject
     */
    public static JSONObject jsonToObj(String jsonStr) {
        return BaseUtils.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr) : new JSONObject();
    }

    /**
     * @param jsonStr json字符串
     * @param clz     转换对象class
     * @return Object 转换对象
     * @description json 转 对象
     */
    public static Object jsonToObj(String jsonStr, Class<?> clz) {
        return BaseUtils.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr, clz) : new JSONObject();
    }

    /**
     * @param obj 转换对象
     * @return String json字符串
     * @description 对象 转 json
     */
    public static <T> List<T> objToJsonArray(Object obj, Class<T> clz) {
        return BaseUtils.isNotEmpty(obj) ? JSON.parseArray(objToJson(obj), clz) : new ArrayList<>();
    }

    /**
     * @param jsonStr json字符串
     * @param obj     转换对象
     * @return Object 转换对象
     * @description json 转 对象
     */
    public static Object jsonToObj(String jsonStr, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        // json字符串转JSONObject对象
        JSONObject jsonObj = jsonToObj(jsonStr);
        // 断言jsonObj不能为空
        if (jsonObj == null) {
            throw new AssertionError();
        }
        // JSONObject对象数据转换为Map集合
        Map<String, Object> jsonObjInnerMap = jsonObj.getInnerMap();
        // 获取当前对象class
        Class<?> cls = ClassUtils.newClass(obj);
        // 获取当前对象所有属性(包含父类属性)
        Field[] fields = ClassUtils.getAllDeclaredFields(cls);
        // set写入方法
        Method setMethod;

        Set<Map.Entry<String, Object>> jsonEntries = jsonObjInnerMap.entrySet();
        for (Map.Entry<String, Object> jsonEntry : jsonEntries) {

            String jsonEntryKey = jsonEntry.getKey();
            Object jsonEntryVal = jsonEntry.getValue();

            for (Field field : fields) {

                String fieldName = field.getName();
                String fieldType = field.getType().getName();

                if (BaseUtils.isNotEmpty(jsonEntryVal) && fieldName.equals(jsonEntryKey)) {

                    // 获取对象属性方法
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, cls);
                    // 获取set写入方法
                    setMethod = pd.getWriteMethod();

                    /* java类 */
                    if (fieldType.contains("java") || ClassUtils.containBaseType(fieldType)) {

                        /* java工具类 */
                        if (fieldType.contains("util")) {

                            /* 日期类型 */
                            if (fieldType.contains("Date")) {
                                /* 时间戳数据 */
                                if (jsonEntryVal instanceof Long) {
                                    Long dateL = new Long(jsonEntryVal.toString());
                                    // 将实例化对象写入当前属性值
                                    setMethod.invoke(obj, new Date(dateL));

                                    /* 字符串 */
                                } else {
                                    Date date = DateUtils.strDateFormat(jsonEntryVal.toString());
                                    // 将实例化对象写入当前属性值
                                    setMethod.invoke(obj, date);
                                }

                                /* list集合 */
                            } else if (fieldType.contains("List")) {
                                // 获取集合中的泛型类型
                                String fieldTypeName = field.getGenericType().getTypeName();
                                // 获取当前属性的class对象
                                Class<?> fieldClass = ClassUtils.newClass(fieldTypeName, ClassUtils.LIST_TYPE);
                                // 当前属性class对象实列化
                                Object fieldInstance = fieldClass.newInstance();
                                // 对象 转 json
                                List<?> jsonArray = objToJsonArray(jsonEntryVal, fieldClass);
                                // 属性集合
                                List<Object> fieldList = new ArrayList<>();
                                for (Object json : jsonArray) {
                                    String objToJson = objToJson(json);
                                    // 方法递归
                                    Object newInstance = jsonToObj(objToJson, fieldInstance);
                                    // 递归返回对象插入集合
                                    fieldList.add(newInstance);
                                }
                                // 将实例化对象写入当前属性值
                                setMethod.invoke(obj, fieldList);
                            } else if (fieldType.contains("Map")) {

                                // 将实例化对象写入当前属性值
                                setMethod.invoke(obj, jsonEntryVal);

                            }
                            /* java 基础数据类型、基础数据引用类型 */
                        } else {
                            if (fieldType.contains("StringBuffer")) {
                                StringBuffer strBufferVal = new StringBuffer(jsonEntryVal.toString());
                                // 将实例化对象写入当前属性值
                                setMethod.invoke(obj, strBufferVal);
                            } else if (fieldType.contains("StringBuilder")) {
                                StringBuilder strBuilderVal = new StringBuilder(jsonEntryVal.toString());
                                // 将实例化对象写入当前属性值
                                setMethod.invoke(obj, strBuilderVal);
                            } else {
                                // 将实例化对象写入当前属性值
                                setMethod.invoke(obj, jsonEntryVal);
                            }
                        }
                        /* 非java类 */
                    } else {
                        // 当前属性实列化对象
                        Object fieldInstance = ClassUtils.newInstance(fieldType);
                        // 对象 转 json
                        String jsonString = objToJson(jsonEntryVal);
                        // 方法递归
                        jsonToObj(jsonString, fieldInstance);
                        // 将实例化对象写入当前属性值
                        setMethod.invoke(obj, fieldInstance);
                    }
                }
            }
        }
        return obj;
    }

}
