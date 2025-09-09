package org.tools.bedrock.util;

import java.util.Objects;

/**
 * 断言工具类，用于参数校验和条件检查
 *
 * @author sander
 * @version 1.0
 */
public class Aver {

    private Aver() {
    }

    /**
     * 断言对象不为null
     *
     * @param obj     待检查对象
     * @param message 异常信息
     * @throws IllegalArgumentException 当对象为null时抛出
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象不为null（默认异常信息）
     *
     * @param obj 待检查对象
     * @throws IllegalArgumentException 当对象为null时抛出
     */
    public static void notNull(Object obj) {
        notNull(obj, "Object must not be null");
    }

    /**
     * 断言字符串不为null且不为空
     *
     * @param str     待检查字符串
     * @param message 异常信息
     * @throws IllegalArgumentException 当字符串为null或空时抛出
     */
    public static void notBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串不为null且不为空（默认异常信息）
     *
     * @param str 待检查字符串
     * @throws IllegalArgumentException 当字符串为null或空时抛出
     */
    public static void notBlank(String str) {
        notBlank(str, "String must not be blank");
    }

    /**
     * 断言两个对象不相等
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param message  异常信息
     * @throws IllegalArgumentException 当两个对象相等时抛出
     */
    public static void notEquals(Object expected, Object actual, String message) {
        if (Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言两个对象不相等（默认异常信息）
     *
     * @param expected 期望值
     * @param actual   实际值
     * @throws IllegalArgumentException 当两个对象相等时抛出
     */
    public static void notEquals(Object expected, Object actual) {
        notEquals(expected, actual, "Objects must not be equal. Unexpected: " + actual);
    }

    /**
     * 断言字符串不为空（null或空字符串）
     *
     * @param str     待检查字符串
     * @param message 异常信息
     * @throws IllegalArgumentException 当字符串为空时抛出
     */
    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串不为空（默认异常信息）
     *
     * @param str 待检查字符串
     * @throws IllegalArgumentException 当字符串为空时抛出
     */
    public static void notEmpty(String str) {
        notEmpty(str, "String must not be empty");
    }

    /**
     * 断言数组不为空
     *
     * @param array   待检查数组
     * @param message 异常信息
     * @throws IllegalArgumentException 当数组为空时抛出
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数组不为空（默认异常信息）
     *
     * @param array 待检查数组
     * @throws IllegalArgumentException 当数组为空时抛出
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "Array must not be empty");
    }

    /**
     * 断言集合不为空
     *
     * @param collection 待检查集合
     * @param message    异常信息
     * @throws IllegalArgumentException 当集合为空时抛出
     */
    public static void notEmpty(java.util.Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言集合不为空（默认异常信息）
     *
     * @param collection 待检查集合
     * @throws IllegalArgumentException 当集合为空时抛出
     */
    public static void notEmpty(java.util.Collection<?> collection) {
        notEmpty(collection, "Collection must not be empty");
    }

    /**
     * 断言Map不为空
     *
     * @param map     待检查Map
     * @param message 异常信息
     * @throws IllegalArgumentException 当Map为空时抛出
     */
    public static void notEmpty(java.util.Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言Map不为空（默认异常信息）
     *
     * @param map 待检查Map
     * @throws IllegalArgumentException 当Map为空时抛出
     */
    public static void notEmpty(java.util.Map<?, ?> map) {
        notEmpty(map, "Map must not be empty");
    }

    /**
     * 断言表达式为真
     *
     * @param expression 待检查表达式
     * @param message    异常信息
     * @throws IllegalArgumentException 当表达式为false时抛出
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言表达式为真（默认异常信息）
     *
     * @param expression 待检查表达式
     * @throws IllegalArgumentException 当表达式为false时抛出
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "Expression must be true");
    }

    /**
     * 断言两个对象相等
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param message  异常信息
     * @throws IllegalArgumentException 当两个对象不相等时抛出
     */
    public static void equals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言两个对象相等
     *
     * @param expected 期望值
     * @param actual   实际值
     * @param message  异常信息
     * @throws IllegalArgumentException 当两个对象不相等时抛出
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言两个对象相等（默认异常信息）
     *
     * @param expected 期望值
     * @param actual   实际值
     * @throws IllegalArgumentException 当两个对象不相等时抛出
     */
    public static void equals(Object expected, Object actual) {
        equals(expected, actual, "Objects are not equal. Expected: " + expected + ", Actual: " + actual);
    }

    /**
     * 断言两个对象相等（默认异常信息）
     *
     * @param expected 期望值
     * @param actual   实际值
     * @throws IllegalArgumentException 当两个对象不相等时抛出
     */
    public static void assertEquals(Object expected, Object actual) {
        equals(expected, actual, "Objects are not equal. Expected: " + expected + ", Actual: " + actual);
    }

    /**
     * 断言数值大于0
     *
     * @param number  待检查的数值
     * @param message 异常信息
     * @throws IllegalArgumentException 当数值不大于0时抛出
     */
    public static void positive(Number number, String message) {
        if (number == null || number.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数值大于0（默认异常信息）
     *
     * @param number 待检查的数值
     * @throws IllegalArgumentException 当数值不大于0时抛出
     */
    public static void positive(Number number) {
        positive(number, "Number must be positive");
    }

    /**
     * 断言数值大于等于0
     *
     * @param number  待检查的数值
     * @param message 异常信息
     * @throws IllegalArgumentException 当数值小于0时抛出
     */
    public static void nonNegative(Number number, String message) {
        if (number == null || number.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数值大于等于0（默认异常信息）
     *
     * @param number 待检查的数值
     * @throws IllegalArgumentException 当数值小于0时抛出
     */
    public static void nonNegative(Number number) {
        nonNegative(number, "Number must be non-negative");
    }

    /**
     * 断言数值小于指定值
     *
     * @param number   待检查的数值
     * @param lessThan 上限值
     * @param message  异常信息
     * @throws IllegalArgumentException 当数值不小于指定值时抛出
     */
    public static void lessThan(Number number, Number lessThan, String message) {
        if (number == null || lessThan == null || number.doubleValue() >= lessThan.doubleValue()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数值小于指定值（默认异常信息）
     *
     * @param number   待检查的数值
     * @param lessThan 上限值
     * @throws IllegalArgumentException 当数值不小于指定值时抛出
     */
    public static void lessThan(Number number, Number lessThan) {
        lessThan(number, lessThan, "Number must be less than " + lessThan);
    }

    /**
     * 断言数值大于指定值
     *
     * @param number      待检查的数值
     * @param greaterThan 下限值
     * @param message     异常信息
     * @throws IllegalArgumentException 当数值不大于指定值时抛出
     */
    public static void greaterThan(Number number, Number greaterThan, String message) {
        if (number == null || greaterThan == null || number.doubleValue() <= greaterThan.doubleValue()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数值大于指定值（默认异常信息）
     *
     * @param number      待检查的数值
     * @param greaterThan 下限值
     * @throws IllegalArgumentException 当数值不大于指定值时抛出
     */
    public static void greaterThan(Number number, Number greaterThan) {
        greaterThan(number, greaterThan, "Number must be greater than " + greaterThan);
    }

    /**
     * 断言数值在指定范围内（包含边界）
     *
     * @param number  待检查的数值
     * @param min     最小值
     * @param max     最大值
     * @param message 异常信息
     * @throws IllegalArgumentException 当数值不在指定范围内时抛出
     */
    public static void inRange(Number number, Number min, Number max, String message) {
        if (number == null || min == null || max == null ||
                number.doubleValue() < min.doubleValue() || number.doubleValue() > max.doubleValue()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数值在指定范围内（包含边界，默认异常信息）
     *
     * @param number 待检查的数值
     * @param min    最小值
     * @param max    最大值
     * @throws IllegalArgumentException 当数值不在指定范围内时抛出
     */
    public static void inRange(Number number, Number min, Number max) {
        inRange(number, min, max, "Number must be between " + min + " and " + max + " (inclusive)");
    }

    /**
     * 断言对象为null
     *
     * @param obj     待检查对象
     * @param message 异常信息
     * @throws IllegalArgumentException 当对象不为null时抛出
     */
    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象为null（默认异常信息）
     *
     * @param obj 待检查对象
     * @throws IllegalArgumentException 当对象不为null时抛出
     */
    public static void isNull(Object obj) {
        isNull(obj, "Object must be null");
    }

    /**
     * 断言字符串为null或空
     *
     * @param str     待检查字符串
     * @param message 异常信息
     * @throws IllegalArgumentException 当字符串不为null或空时抛出
     */
    public static void isBlank(String str, String message) {
        if (str != null && !str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串为null或空（默认异常信息）
     *
     * @param str 待检查字符串
     * @throws IllegalArgumentException 当字符串不为null或空时抛出
     */
    public static void isBlank(String str) {
        isBlank(str, "String must be blank");
    }

    /**
     * 断言表达式为假
     *
     * @param expression 待检查表达式
     * @param message    异常信息
     * @throws IllegalArgumentException 当表达式为true时抛出
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言表达式为假（默认异常信息）
     *
     * @param expression 待检查表达式
     * @throws IllegalArgumentException 当表达式为true时抛出
     */
    public static void isFalse(boolean expression) {
        isFalse(expression, "Expression must be false");
    }

    public static void assertFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}
