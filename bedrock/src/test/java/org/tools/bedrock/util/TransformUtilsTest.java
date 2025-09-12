package org.tools.bedrock.util;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

/**
 * TransformUtils 冒烟测试
 * 测试基本的转换功能
 */
public class TransformUtilsTest {

    @Test
    public void testStrToChar() {
        // 测试字符串转字符数组
        String testStr = "Hello World";
        char[] chars = TransformUtils.strToChar(testStr);

        assertNotNull("转换后的字符数组不应为null", chars);
        assertEquals("字符数组长度应与字符串长度相同", testStr.length(), chars.length);
        assertArrayEquals("字符数组内容应与原字符串一致", testStr.toCharArray(), chars);
    }

    @Test
    public void testStrToCharWithEmptyString() {
        // 测试空字符串转字符数组
        String emptyStr = "";
        char[] chars = TransformUtils.strToChar(emptyStr);

        assertNotNull("转换后的字符数组不应为null", chars);
        assertEquals("空字符串应转换为空字符数组", 0, chars.length);
    }

    @Test
    public void testStrToCharWithNull() {
        // 测试null字符串转字符数组会抛出异常
        try {
            TransformUtils.strToChar(null);
            fail("应抛出NullPointerException");
        } catch (NullPointerException e) {
            // 预期的异常
            assertTrue("应抛出NullPointerException", e instanceof NullPointerException);
        }
    }

    @Test
    public void testCharToStr() {
        // 测试字符数组转字符串
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        String str = TransformUtils.charToStr(chars);

        assertNotNull("转换后的字符串不应为null", str);
        assertEquals("字符串内容应与字符数组一致", "Hello", str);
    }

    @Test
    public void testCharToStrWithEmptyArray() {
        // 测试空字符数组转字符串
        char[] emptyChars = {};
        String str = TransformUtils.charToStr(emptyChars);

        assertNotNull("转换后的字符串不应为null", str);
        assertEquals("空字符数组应转换为空字符串", "", str);
    }

    @Test
    public void testCharToStrWithNull() {
        // 测试null字符数组转字符串会抛出异常
        try {
            TransformUtils.charToStr(null);
            fail("应抛出NullPointerException");
        } catch (NullPointerException e) {
            // 预期的异常
            assertTrue("应抛出NullPointerException", e instanceof NullPointerException);
        }
    }

    @Test
    public void testStrToByte() {
        // 测试字符串转字节数组（默认编码）
        String testStr = "Hello World";
        byte[] bytes = TransformUtils.strToByte(testStr);

        assertNotNull("转换后的字节数组不应为null", bytes);
        assertTrue("字节数组长度应大于0", bytes.length > 0);
        assertArrayEquals("字节数组内容应与原字符串一致", testStr.getBytes(), bytes);
    }

    @Test
    public void testStrToByteWithCharset() {
        // 测试字符串转字节数组（指定编码）
        String testStr = "Hello World";
        byte[] bytes = TransformUtils.strToByte(testStr, "UTF-8");

        assertNotNull("转换后的字节数组不应为null", bytes);
        assertTrue("字节数组长度应大于0", bytes.length > 0);
        assertArrayEquals("字节数组内容应与原字符串一致", testStr.getBytes(StandardCharsets.UTF_8), bytes);
    }

    @Test
    public void testStrToByteWithInvalidCharset() {
        // 测试字符串转字节数组（无效编码）
        String testStr = "Hello World";
        byte[] bytes = TransformUtils.strToByte(testStr, "INVALID-CHARSET");

        // 应返回空字节数组而不是抛出异常
        assertNotNull("转换后的字节数组不应为null", bytes);
        assertEquals("无效编码应返回空字节数组", 0, bytes.length);
    }

    @Test
    public void testStrToByteWithEmptyString() {
        // 测试空字符串转字节数组
        String emptyStr = "";
        byte[] bytes = TransformUtils.strToByte(emptyStr);

        assertNotNull("转换后的字节数组不应为null", bytes);
        assertEquals("空字符串应转换为空字节数组", 0, bytes.length);
    }

    @Test
    public void testStrToByteWithNull() {
        // 测试null字符串转字节数组会抛出异常
        try {
            TransformUtils.strToByte(null);
            fail("应抛出NullPointerException");
        } catch (NullPointerException e) {
            // 预期的异常
            assertTrue("应抛出NullPointerException", e instanceof NullPointerException);
        }
    }

    @Test
    public void testByteToStr() {
        // 测试字节数组转字符串（默认编码）
        String testStr = "Hello World";
        byte[] bytes = testStr.getBytes();
        String str = TransformUtils.byteToStr(bytes);

        assertNotNull("转换后的字符串不应为null", str);
        assertEquals("字符串内容应与原字节数组一致", testStr, str);
    }

    @Test
    public void testByteToStrWithCharset() {
        // 测试字节数组转字符串（指定编码）
        String testStr = "Hello World";
        byte[] bytes = testStr.getBytes(StandardCharsets.UTF_8);
        String str = TransformUtils.byteToStr(bytes, "UTF-8");

        assertNotNull("转换后的字符串不应为null", str);
        assertEquals("字符串内容应与原字节数组一致", testStr, str);
    }

    @Test
    public void testByteToStrWithEmptyArray() {
        // 测试空字节数组转字符串
        byte[] emptyBytes = {};
        String str = TransformUtils.byteToStr(emptyBytes);

        assertNotNull("转换后的字符串不应为null", str);
        assertEquals("空字节数组应转换为空字符串", "", str);
    }

    @Test
    public void testByteToStrWithNull() {
        // 测试null字节数组转字符串会抛出异常
        try {
            TransformUtils.byteToStr(null);
            fail("应抛出NullPointerException");
        } catch (NullPointerException e) {
            // 预期的异常
            assertTrue("应抛出NullPointerException", e instanceof NullPointerException);
        }
    }

    @Test
    public void testRoundTripConversion() {
        // 测试往返转换：字符串->字节数组->字符串
        String original = "Hello 世界";
        byte[] bytes = TransformUtils.strToByte(original, "UTF-8");
        String result = TransformUtils.byteToStr(bytes, "UTF-8");

        assertNotNull("转换后的字符串不应为null", result);
        assertEquals("往返转换后字符串应保持一致", original, result);
    }

    @Test
    public void testChineseCharacters() {
        // 测试中文字符转换
        String chinese = "你好世界";
        byte[] bytes = TransformUtils.strToByte(chinese, "UTF-8");
        String result = TransformUtils.byteToStr(bytes, "UTF-8");

        assertNotNull("转换后的字符串不应为null", result);
        assertEquals("中文字符转换应保持一致", chinese, result);
    }
}
