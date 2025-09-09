package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.constant.Constants;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 跨对象传值优化
 * 继承对象参数copy
 * 属性子类参数copy
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
public class ClassUtils {

    private ClassUtils() {
    }

    /**
     * java基础数据类型
     */
    protected static final String[] BASE_TYPE = {"byte", "short", "int", "long", "float", "double", "boolean", "char"};
    protected static final String[] PACK_TYPE = {"Byte", "BitSieve", "Short", "Integer", "BigInteger", "Long", "Float", "Double", "BigDecimal", "Boolean", "Char"};
    /**
     * list类型
     */
    public static final String LIST_TYPE = "list";
    /**
     * map类型
     */
    public static final String MAP_TYPE = "map";
    /**
     * get方法
     */
    public static final String GET_METHOD_TYPE = "get";
    /**
     * set方法
     */
    public static final String SET_METHOD_TYPE = "set";

    /**
     * 是java基础类
     *
     * @param fieldType 类型
     * @return boolean
     */
    public static boolean isBaseClass(String fieldType) {
        for (String baseType : BASE_TYPE) {
            if (baseType.equals(fieldType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnumClass(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Class)) {
            return false;
        }
        Class<?> clazz = (Class<?>) obj;
        return clazz.isEnum();
    }

    public static boolean nativeClass(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Comparable;
    }

    /**
     * 获取当前对象属性class
     *
     * @param clsTypeName 属性类型名称
     * @return class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class<?> newClass(String clsTypeName) throws ClassNotFoundException {
        // 获取当前属性的class对象
        return Class.forName(clsTypeName);
    }

    /**
     * 集合属性class
     *
     * @param clsTypeName 属性类型名称
     * @param lob         接口类型
     * @return class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class<?> newClass(String clsTypeName, String lob) throws ClassNotFoundException {
        String subFieldTypeName;
        if (LIST_TYPE.equals(lob) || MAP_TYPE.equals(lob)) {
            // 截取正确的泛型对象类型
            subFieldTypeName = clsTypeName.substring(clsTypeName.indexOf("<") + 1, clsTypeName.length() - 1);
        } else {
            return newClass(clsTypeName);
        }
        return newClass(subFieldTypeName);
    }

    /**
     * 获取当前对象父类属性class
     *
     * @param cls class
     * @return 父类class
     */
    public static Class<?> newSuperClass(Class<?> cls) {
        // 获取当前属性父类的class对象
        return cls.getSuperclass();
    }

    /**
     * 获取当前对象父类属性class
     *
     * @param cls class
     * @return 父类class
     */
    public static Class<?>[] newSupersClass(Class<?> cls) {
        boolean existSuper = true;
        Integer classIndex = 0;
        Class<?> aClass = newSuperClass(cls);
        if (aClass == null) {
            classIndex--;
            existSuper = false;
        }
        while (existSuper) {
            classIndex++;
            aClass = newSuperClass(aClass);
            if (aClass == null) {
                aClass = newSuperClass(cls);
                classIndex--;
                existSuper = false;
            }
        }
        if (classIndex.compareTo(Constants.ZERO_I) < Constants.ONE_I) {
            return new Class[0];
        }
        Class<?>[] supersClass = new Class<?>[classIndex];
        supersClass[0] = aClass;
        for (int i = 1; i < classIndex; i++) {
            aClass = newSuperClass(aClass);
            supersClass[i] = aClass;
        }

        // 获取当前属性父类的class对象
        return supersClass;
    }

    /**
     * 获取当前对象父类属性class
     *
     * @param obj 对象
     * @return 父类class
     */
    public static Class<?> newSuperClass(Object obj) {
        // 获取当前属性父类的class对象
        return obj.getClass().getSuperclass();
    }

    /**
     * 获取当前对象父类属性class
     *
     * @param clsTypeName 属性类型名称
     * @return class
     * @throws ClassNotFoundException 找不到类
     */
    public static Class<?> newSuperClass(String clsTypeName) throws ClassNotFoundException {
        // 获取当前属性父类的class对象
        return newClass(clsTypeName).getSuperclass();
    }


    /**
     * 获取当前对象所有属性
     *
     * @param cls 属性类型class对象
     * @return Field[]
     */
    public static Field[] getDeclaredFields(Class<?> cls) {
        // 获取当前对象所有私有属性
        return cls.getDeclaredFields();
    }

    /**
     * 获取当前对象所有属性
     *
     * @param clsTypeName 属性类型名称
     * @return Field[]
     * @throws ClassNotFoundException 找不到类
     */
    public static Field[] getDeclaredFields(String clsTypeName) throws ClassNotFoundException {
        Class<?> cls = newClass(clsTypeName);
        // 获取当前对象所有私有属性
        return cls.getDeclaredFields();
    }

    /**
     * 获取当前对象父类所有属性
     *
     * @param cls 属性类型class对象
     * @return Field[]
     */
    public static Field[] getSuperDeclaredFields(Class<?> cls) {
        Class<?> superCls = newSuperClass(cls);
        Field[] files = superCls.getDeclaredFields();
        Class<?>[] supersCls = newSupersClass(superCls);
        if (BaseUtils.isNotEmpty(supersCls) && supersCls.length > 0) {
            int filesize = files.length;
            List<Field[]> fileList = new ArrayList<>();
            fileList.add(files);
            for (Class<?> supersCl : supersCls) {
                superCls = supersCl;
                files = superCls.getDeclaredFields();
                fileList.add(files);
            }
            for (Field[] fields : fileList) {
                filesize = filesize + fields.length;
            }
            files = new Field[filesize];
            for (Field[] fields : fileList) {
                System.arraycopy(fields, 0, files, 0, filesize);
            }
        }
        // 获取当前对象所有私有属性
        return files;
    }

    /**
     * 获取当前对象父类所有属性
     *
     * @param clsTypeName 属性类型名称
     * @return Field[]
     * @throws ClassNotFoundException 找不到类
     */
    public static Field[] getSuperDeclaredFields(String clsTypeName) throws ClassNotFoundException {
        Class<?> cls = newSuperClass(clsTypeName);
        // 获取当前对象所有私有属性
        return cls.getDeclaredFields();
    }

    /**
     * 获取当前对象所有属性(包含父类属性)
     *
     * @param cls class
     * @return Field[]
     */
    public static Field[] getAllDeclaredFields(Class<?> cls) {
        // 获取当前对象所有私有属性
        Field[] thisFields = getDeclaredFields(cls);
        // 获取当前对象父类所有私有属性
        Field[] superFields = getSuperDeclaredFields(cls);
        // 获取当前对象私有属性长度
        int thisFieldsLen = thisFields.length;
        // 获取当前对象父类私有属性长度
        int superFieldsLen = superFields.length;
        // 设定子类 + 父类所有私有属性长度
        int fieldsLen = thisFieldsLen + superFieldsLen;
        Field[] fields = new Field[fieldsLen];
        // 遍历数组将当前对象和其父类所有属性合并
        for (int i = 0; i < fieldsLen; i++) {
            if (i < thisFieldsLen) {
                fields[i] = thisFields[i];
            } else {
                fields[i] = superFields[i - thisFieldsLen];
            }
        }
        return fields;
    }

    /**
     * 获取当前对象所有属性(包含父类属性)
     *
     * @param clsTypeName 属性类型名称
     * @return Field[]
     * @throws ClassNotFoundException 找不到类
     */
    public static Field[] getAllDeclaredFields(String clsTypeName) throws ClassNotFoundException {
        Class<?> cls = newClass(clsTypeName);
        return getAllDeclaredFields(cls);
    }

    /**
     * 去除方法未找到的属性
     *
     * @param fields 属性数组
     * @param cls    class
     * @return Field[]
     */
    public static Field[] roverMethodNotFoundDeclaredFields(Field[] fields, Class<?> cls) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                getReadMethod(fieldName, cls);
                fieldList.add(field);
            } catch (IntrospectionException e) {
                log.info(e.getMessage(), e);
            }
        }
        return fieldList.toArray(new Field[0]);
    }

    /**
     * 实例化对象
     *
     * @param cls class
     * @return Object
     */
    public static Object newInstance(Class<?> cls) {
        try {
            // 对象实列化
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 实例化对象
     *
     * @param clsTypeName 属性类型名称
     * @return Object
     * @throws ClassNotFoundException 找不到类
     * @throws InstantiationException 实例化
     * @throws IllegalAccessException 非法访问
     */
    public static Object newInstance(String clsTypeName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 获取当前属性的class对象
        Class<?> fieldClass = newClass(clsTypeName);
        // 对象实列化
        return fieldClass.newInstance();
    }

    /**
     * 实例化对象
     *
     * @param clsTypeName 集合属性类型名称
     * @param lob         接口类型
     * @return Object
     * @throws ClassNotFoundException 找不到类
     * @throws InstantiationException 实例化
     * @throws IllegalAccessException 非法访问
     */
    public static Object newInstance(String clsTypeName, String lob) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 获取当前属性的class对象
        Class<?> fieldClass = newClass(clsTypeName, lob);
        // 对象实列化
        return fieldClass.newInstance();
    }

    /**
     * get属性方法
     *
     * @param fieldName 属性名称
     * @param cls       class对象
     * @return Method
     * @throws IntrospectionException 反省
     */
    public static Method getReadMethod(String fieldName, Class<?> cls) throws IntrospectionException {
        // 获取对象属性方法
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, cls);
        // 获取set写入方法
        return pd.getReadMethod();
    }

    /**
     * set属性方法
     *
     * @param fieldName 属性名称
     * @param cls       class对象
     * @return Method
     * @throws IntrospectionException 反省
     */
    public static Method getWriteMethod(String fieldName, Class<?> cls) throws IntrospectionException {
        // 获取对象属性方法
        PropertyDescriptor pd = new PropertyDescriptor(fieldName, cls);
        // 获取set写入方法
        return pd.getWriteMethod();
    }

    public static <T> Object getProperty(T t, String fieldName) {
        Class<?> aClass = t.getClass();
        try {
            Method writeMethod = getReadMethod(fieldName, aClass);
            return writeMethod.invoke(t);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            log.info("读取属性失败：", e);
        }
        return null;
    }

    public static <T> Boolean setProperty(T t, String fieldName, Object fieldValue) {
        Class<?> aClass = t.getClass();
        try {
            Method writeMethod = getWriteMethod(fieldName, aClass);
            writeMethod.invoke(t, fieldValue);
            return true;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            log.info("写入属性失败：", e);
        }
        return false;
    }

    /**
     * 获取属性方法
     *
     * @param fieldName  属性名称
     * @param methodType 方法类型
     * @param cls        class对象
     * @return Method
     * @throws IntrospectionException 反省
     */
    public static Method getMethod(String fieldName, String methodType, Class<?> cls) throws IntrospectionException {
        if (GET_METHOD_TYPE.equals(methodType)) {
            return getReadMethod(fieldName, cls);
        }
        if (SET_METHOD_TYPE.equals(methodType)) {
            return getWriteMethod(fieldName, cls);
        }
        return null;
    }

    /**
     * 字符串分隔符首字母大写
     * str_separator_first_capitalize = strSeparatorFirstCapitalize
     *
     * @param fieldName 字段名称
     * @return 驼峰命名法
     */
    public static String strSeparatorFirstCapitalize(String fieldName) {
        if (fieldName.contains("_")) {
            int indexOf = fieldName.indexOf("_");
            int length = fieldName.length();
            if (indexOf >= length - 1) {
                return fieldName.replace("_", "");
            }
            String strReplace = fieldName.substring(indexOf);
            String substring = fieldName.substring(indexOf + 1);
            String firstCapitalize = strFirstCapitalize(substring);
            fieldName = fieldName.replaceAll(strReplace, firstCapitalize);
            fieldName = strSeparatorFirstCapitalize(fieldName);
        }
        return fieldName;
    }

    /**
     * 字符串首字母大写
     * str = Str
     *
     * @param fieldName 字段名称
     * @return 首字母大写
     */
    public static String strFirstCapitalize(String fieldName) {
        if (BaseUtils.isNotEmpty(fieldName)) {
            byte[] items = fieldName.getBytes();
            items[0] = (byte) ((char) items[0] - 'a' + 'A');
            return new String(items);
        }
        return fieldName;
    }

    public static <O> Field[] fetchFields(Class<O> clz) {
        Map<String, Field> fieldMap = new HashMap<>();
        fetchAllFields(clz.getDeclaredFields(), clz, fieldMap);
        Field[] newFields = new Field[0];
        return fieldMap.values().toArray(newFields);
    }

    /**
     * 父类数量
     *
     * @param clz 类信息
     * @return 父类数量
     */
    public static int superSize(Class<?> clz) {
        int superSize = 0;
        Class<?> superclass = clz.getSuperclass();
        // Use an "instanceof" comparison instead. 请忽略该预警，因为所有的对象都会继承自Object类。
        if (BaseUtils.isNotNull(superclass) && !"Object".equals(superclass.getSimpleName())) {
            superSize = superSize + 1;
            superSize = superSize + superSize(superclass);
        }
        return superSize;
    }

    public static void fetchAllFields(Field[] fields, Class<?> clz, Map<String, Field> fieldMap) {
        for (Field field : fields) {
            try {
                fetchField(field, clz, fieldMap);
            } catch (IntrospectionException e) {
                log.info(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void fetchField(Field field, Class<?> clz, Map<String, Field> fieldMap) throws IntrospectionException {
        String fieldName = handleBooleanType(field);
        new PropertyDescriptor(fieldName, clz);
        fieldMap.put(fieldName, field);
    }

    /**
     * 处理布尔类型
     *
     * @param field 属性
     * @return 属性名
     */
    public static String handleBooleanType(Field field) {
        String fieldName = field.getName();
        // 处理布尔类型
        List<String> bool = Arrays.asList("boolean", "Boolean");
        if (bool.contains(field.getType().getSimpleName())) {
            //  处理is前缀
            fieldName = fieldName.startsWith("is") ? fieldName.substring(2) : fieldName;
        }
        return fieldName;
    }

    public static <O> Field[] fetchAllFields(Class<O> clz) {
        Map<String, Field> fieldMap = new HashMap<>();

        fetchAllFields(clz.getDeclaredFields(), clz, fieldMap);

        int superSize = superSize(clz);
        if (superSize > 0) {
            Class<?> superClz = clz;
            for (int i = 0; i < superSize; i++) {
                superClz = superClz.getSuperclass();
                fetchAllFields(superClz.getDeclaredFields(), superClz, fieldMap);
            }
        }
        Field[] newFields = new Field[0];
        return fieldMap.values().toArray(newFields);
    }

    /**
     * 获取两个类中的共有字段
     *
     * @param clz1     类信息
     * @param clz2     类信息
     * @param typeSame 类型相同 default false
     * @return 共有字段数组
     */
    public static Field[] commonFields(Class<?> clz1, Class<?> clz2, boolean typeSame) {
        // 获取两个类中的字段
        return sieveRepeatsFields(fetchFields(clz1), fetchFields(clz2), typeSame);
    }

    /**
     * 获取两个类中的共有字段
     *
     * @param clz1     类信息
     * @param clz2     类信息
     * @param typeSame 类型相同 default false
     * @return 共有字段数组
     */
    public static Field[] allCommonFields(Class<?> clz1, Class<?> clz2, boolean typeSame) {
        // 获取两个类中的字段
        return sieveRepeatsFields(fetchAllFields(clz1), fetchAllFields(clz2), typeSame);
    }

    /**
     * 筛重复字段
     *
     * @param fields1  字段数组
     * @param fields2  字段数组
     * @param typeSame 类型相同
     * @return 重复字段数组
     */
    public static Field[] sieveRepeatsFields(Field[] fields1, Field[] fields2, boolean typeSame) {
        List<Field> fields = new ArrayList<>();
        String fieldName1;
        String fieldName2;
        String fieldType1;
        String fieldType2;
        for (Field field1 : fields1) {
            fieldName1 = field1.getName();
            fieldType1 = field1.getType().getTypeName();
            for (Field field2 : fields2) {
                fieldName2 = field2.getName();
                fieldType2 = field2.getType().getTypeName();
                if (!fieldName1.equals(fieldName2)) {
                    continue;
                }
                if (typeSame) {
                    if (fieldType1.equals(fieldType2)) {
                        fields.add(field2);
                    }
                } else {
                    fields.add(field2);
                }
            }
        }
        Field[] newFields = new Field[0];
        return fields.toArray(newFields);
    }

}
