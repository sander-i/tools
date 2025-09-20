package org.tools.bedrock.util;

import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

/**
 * JsonUtil 冒烟测试
 * 测试基本的JSON转换功能
 */
public class JsonUtilTest {

    @Test
    public void testMapToJson() {
        // 测试基本的Map转JSON功能
        Map<String, Object> map = new HashMap<>();
        map.put("name", "test");
        map.put("age", 25);
        map.put("active", true);

        String json = JsonUtil.mapToJson(map);
        assertNotNull("转换后的JSON不应为null", json);
        assertFalse("转换后的JSON不应为空", json.isEmpty());
        assertTrue("JSON应包含键值对", json.contains("\"name\":\"test\""));
        assertTrue("JSON应包含数字", json.contains("\"age\":\"25\""));
        assertTrue("JSON应包含布尔值", json.contains("\"active\":\"true\""));
    }

    @Test
    public void testListToJson() {
        // 测试基本的List转JSON功能
        List<String> list = Arrays.asList("apple", "banana", "orange");

        String json = JsonUtil.listToJson(list);
        assertNotNull("转换后的JSON不应为null", json);
        assertFalse("转换后的JSON不应为空", json.isEmpty());
        assertTrue("JSON应包含数组元素", json.contains("\"apple\""));
        assertTrue("JSON应包含数组元素", json.contains("\"banana\""));
        assertTrue("JSON应包含数组元素", json.contains("\"orange\""));
        assertTrue("JSON应以方括号开头和结尾", json.startsWith("[") && json.endsWith("]"));
    }

    @Test
    public void testNestedMapToJson() {
        // 测试嵌套Map转JSON功能
        Map<String, Object> nestedMap = new HashMap<>();
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("city", "Beijing");
        innerMap.put("country", "China");

        nestedMap.put("user", "tester");
        nestedMap.put("address", innerMap);

        String json = JsonUtil.mapToJson(nestedMap);
        assertNotNull("转换后的JSON不应为null", json);
        assertTrue("JSON应包含嵌套对象", json.contains("\"address\":{"));
        assertTrue("JSON应包含嵌套属性", json.contains("\"city\":\"Beijing\""));
    }

    @Test
    public void testNestedListToJson() {
        // 测试嵌套List转JSON功能
        List<Object> nestedList = new ArrayList<>();
        List<String> innerList = Arrays.asList("red", "green", "blue");
        nestedList.add("colors");
        nestedList.add(innerList);

        String json = JsonUtil.listToJson(nestedList);
        assertNotNull("转换后的JSON不应为null", json);
        assertTrue("JSON应包含嵌套数组", json.contains("[\"red\""));
    }

    @Test
    public void testMapToJsonWithSkipNull() {
        // 测试Map转JSON时跳过null值的功能
        Map<String, Object> mapWithNull = new HashMap<>();
        mapWithNull.put("name", "test");
        mapWithNull.put("value", null);
        mapWithNull.put("status", "active");

        String json = JsonUtil.mapToJson(mapWithNull, true);
        assertNotNull("转换后的JSON不应为null", json);
        assertTrue("JSON应包含非null值", json.contains("\"name\":\"test\""));
        assertTrue("JSON应包含非null值", json.contains("\"status\":\"active\""));
        assertFalse("JSON不应包含null值", json.contains("\"value\":\"null\""));
    }

    @Test
    public void testListToJsonWithSkipNull() {
        // 测试List转JSON时跳过null值的功能
        List<Object> listWithNull = new ArrayList<>();
        listWithNull.add("first");
        listWithNull.add(null);
        listWithNull.add("third");

        String json = JsonUtil.listToJson(listWithNull, true);
        assertNotNull("转换后的JSON不应为null", json);
        assertTrue("JSON应包含非null元素", json.contains("\"first\""));
        assertTrue("JSON应包含非null元素", json.contains("\"third\""));
        assertFalse("JSON不应包含null元素", json.contains("\"null\""));
    }

    @Test
    public void testEmptyMapToJson() {
        // 测试空Map转JSON
        Map<String, Object> emptyMap = new HashMap<>();

        String json = JsonUtil.mapToJson(emptyMap);
        assertNull("空Map应转换为null", json);
    }

    @Test
    public void testEmptyListToJson() {
        // 测试空List转JSON
        List<Object> emptyList = new ArrayList<>();

        String json = JsonUtil.listToJson(emptyList);
        assertNull("空List应转换为null", json);
    }

    @Test
    public void testArrayToJson() {
        // 测试数组转JSON功能
        Map<String, Object> mapWithArray = new HashMap<>();
        String[] array = {"one", "two", "three"};
        mapWithArray.put("numbers", array);

        String json = JsonUtil.mapToJson(mapWithArray);
        assertNotNull("转换后的JSON不应为null", json);
        assertTrue("JSON应包含数组元素", json.contains("\"one\""));
        assertTrue("JSON应包含数组元素", json.contains("\"two\""));
        assertTrue("JSON应包含数组元素", json.contains("\"three\""));
    }

    @Test
    public void testDeprecatedMethods() {
        // 测试已弃用的方法仍能正常工作
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("test", "value");

        String json1 = JsonUtil.mapToJSON(testMap);
        String json2 = JsonUtil.mapToJson(testMap);

        assertEquals("已弃用方法和新方法应产生相同结果", json1, json2);

        List<String> testList = Arrays.asList("item");
        String json3 = JsonUtil.listToJSON(testList);
        String json4 = JsonUtil.listToJson(testList);

        assertEquals("已弃用方法和新方法应产生相同结果", json3, json4);
    }
}
