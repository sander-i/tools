package org.tools.bedrock.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AssertTest {

    @Test
    public void testAssertSmoke() {
        // 冒烟测试：验证 Assert 类的基本功能

        // 测试 isNotNull(T obj, String errorMsg, Object... params) 方法
        try {
            String result = Assert.isNotNull("test", "对象不能为空");
            assertEquals("test", result);
        } catch (Exception e) {
            fail("isNotNull 方法对非空对象应不抛出异常");
        }

        // 测试 isNotNull(Object obj) 方法
        try {
            Assert.isNotNull("test");
        } catch (Exception e) {
            fail("isNotNull 方法对非空对象应不抛出异常");
        }

        // 测试 isEmpty(List<?> list) 方法
        List<String> emptyList = new ArrayList<>();
        List<String> nullList = null;
        List<String> nonEmptyList = new ArrayList<>();
        nonEmptyList.add("item");

        assertTrue("空列表应返回true", Assert.isEmpty(emptyList));
        assertTrue("null列表应返回true", Assert.isEmpty(nullList));
        assertFalse("非空列表应返回false", Assert.isEmpty(nonEmptyList));

        // 测试 isEmpty(Map<?, ?> map) 方法
        Map<String, String> emptyMap = new HashMap<>();
        Map<String, String> nullMap = null;
        Map<String, String> nonEmptyMap = new HashMap<>();
        nonEmptyMap.put("key", "value");

        assertTrue("空Map应返回true", Assert.isEmpty(emptyMap));
        assertTrue("nullMap应返回true", Assert.isEmpty(nullMap));
        assertFalse("非空Map应返回false", Assert.isEmpty(nonEmptyMap));

        // 测试 isEmpty(Object obj) 方法
        assertTrue("空字符串应返回true", Assert.isEmpty(""));
        assertTrue("只包含空格的字符串应返回true", Assert.isEmpty("   "));
        assertFalse("非空字符串应返回false", Assert.isEmpty("test"));

        // 测试 isNotEmpty(Object obj) 方法
        assertFalse("null对象应返回false", Assert.isNotEmpty(null));
        assertFalse("空字符串应返回false", Assert.isNotEmpty(""));
        assertFalse("只包含空格的字符串应返回false", Assert.isNotEmpty("   "));
        assertTrue("非空字符串应返回true", Assert.isNotEmpty("test"));

        System.out.println("Assert冒烟测试通过：所有基本功能正常工作");
    }

    @Test
    public void testIsNotNullWithMessage() {
        // 测试 isNotNull 正常情况
        String obj = "test";
        String result = Assert.isNotNull(obj, "对象不能为空");
        assertEquals(obj, result);
    }

    @Test
    public void testIsNotNullWithoutMessage() {
        // 测试 isNotNull 正常情况
        String obj = "test";
        Assert.isNotNull(obj);
        // 不抛出异常即为通过
    }

    @Test
    public void testIsEmptyList() {
        List<String> list = new ArrayList<>();
        assertTrue(Assert.isEmpty(list));
    }

    @Test
    public void testIsEmptyMap() {
        Map<String, String> map = new HashMap<>();
        assertTrue(Assert.isEmpty(map));
    }

    @Test
    public void testIsEmptyObject() {
        assertTrue(Assert.isEmpty(""));
    }

    @Test
    public void testIsNotEmpty() {
        assertTrue(Assert.isNotEmpty("test"));
        assertFalse(Assert.isNotEmpty(""));
    }
}