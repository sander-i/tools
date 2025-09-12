package org.tools.bedrock.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Maps 冒烟测试
 * 测试基本的Map操作功能
 */
public class MapsTest {

    // 测试用的简单POJO类
    public static class Person {
        private String name;
        private int age;
        private boolean active;

        public Person() {
        }

        public Person(String name, int age, boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age &&
                    active == person.active &&
                    Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, active);
        }
    }

    @Test
    public void testIsEmpty() {
        // 测试isEmpty方法
        assertTrue("null Map应为空", Maps.isEmpty(null));
        assertTrue("空Map应为空", Maps.isEmpty(new HashMap<>()));
        assertFalse("非空Map不应为空", Maps.isEmpty(Collections.singletonMap("key", "value")));
    }

    @Test
    public void testIsNotEmpty() {
        // 测试isNotEmpty方法
        assertFalse("null Map应为空", Maps.isNotEmpty(null));
        assertFalse("空Map应为空", Maps.isNotEmpty(new HashMap<>()));
        assertTrue("非空Map应为非空", Maps.isNotEmpty(Collections.singletonMap("key", "value")));
    }

    @Test
    public void testObjToMap() {
        // 测试对象转Map功能
        Person person = new Person("张三", 25, true);
        Map<String, Object> map = Maps.objToMap(person);

        assertNotNull("转换后的Map不应为null", map);
        assertFalse("转换后的Map不应为空", map.isEmpty());
        assertEquals("应包含name属性", "张三", map.get("name"));
        assertEquals("应包含age属性", 25, map.get("age"));
        assertEquals("应包含active属性", true, map.get("active"));
    }

    @Test
    public void testObjToMapWithNull() {
        // 测试null对象转Map
        Map<String, Object> map = Maps.objToMap(null);
        assertNotNull("转换后的Map不应为null", map);
        assertTrue("null对象应转换为空Map", map.isEmpty());
    }

    @Test
    public void testMapToObj() {
        // 测试Map转对象功能
        Map<String, Object> map = new HashMap<>();
        map.put("name", "李四");
        map.put("age", 30);
        map.put("active", false);

        Person person = Maps.mapToObj(map, Person.class);
        assertNotNull("转换后的对象不应为null", person);
        assertEquals("name属性应正确设置", "李四", person.getName());
        assertEquals("age属性应正确设置", 30, person.getAge());
        assertEquals("active属性应正确设置", false, person.isActive());
    }

    @Test
    public void testMapToObjWithExistingObject() {
        // 测试Map转已有对象功能
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王五");
        map.put("age", 35);

        Person person = new Person();
        Person result = Maps.mapToObj(map, person);

        assertSame("应返回相同的对象实例", person, result);
        assertEquals("name属性应正确设置", "王五", person.getName());
        assertEquals("age属性应正确设置", 35, person.getAge());
    }

    @Test
    public void testMapToObjWithRelations() {
        // 测试带关系映射的Map转对象功能
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", "赵六");
        map.put("user_age", 40);

        Map<String, String> relations = new HashMap<>();
        relations.put("user_name", "name");
        relations.put("user_age", "age");

        Person person = Maps.mapToObj(map, relations, Person.class);
        assertNotNull("转换后的对象不应为null", person);
        assertEquals("name属性应通过关系映射正确设置", "赵六", person.getName());
        assertEquals("age属性应通过关系映射正确设置", 40, person.getAge());
    }

    @Test
    public void testMergeMap() {
        // 测试Map合并功能
        Map<String, List<String>> source = new HashMap<>();
        source.put("key1", new ArrayList<>(Arrays.asList("value1", "value2")));
        source.put("key2", new ArrayList<>(Arrays.asList("value3")));

        Map<String, List<String>> target = new HashMap<>();
        target.put("key2", new ArrayList<>(Arrays.asList("value4", "value5")));
        target.put("key3", new ArrayList<>(Arrays.asList("value6")));

        Map<String, List<String>> merged = Maps.mergeMap(source, target);

        assertSame("应返回源Map", source, merged);
        assertEquals("合并后应包含3个键", 3, merged.size());
        assertEquals("key1应保持不变", 2, merged.get("key1").size());
        assertEquals("key2应合并两个列表", 3, merged.get("key2").size());
        assertEquals("key3应新增", 1, merged.get("key3").size());
    }

    @Test
    public void testMergeMapWithVarargs() {
        // 测试多Map合并功能
        Map<String, List<String>> source = new HashMap<>();
        source.put("key1", Arrays.asList("value1"));

        Map<String, List<String>> target1 = new HashMap<>();
        target1.put("key1", Arrays.asList("value2"));
        target1.put("key2", Arrays.asList("value3"));

        Map<String, List<String>> target2 = new HashMap<>();
        target2.put("key2", Arrays.asList("value4"));
        target2.put("key3", Arrays.asList("value5"));

        Map<String, List<String>> merged = Maps.mergeMap(source, target1, target2);

        assertSame("应返回源Map", source, merged);
        assertEquals("合并后应包含3个键", 3, merged.size());
        assertEquals("key1应合并两个列表", 2, merged.get("key1").size());
        assertEquals("key2应合并两个列表", 2, merged.get("key2").size());
        assertEquals("key3应新增", 1, merged.get("key3").size());
    }

    @Test
    public void testMergeMapWithEmptyMaps() {
        // 测试空Map合并
        Map<String, List<String>> source = new HashMap<>();
        Map<String, List<String>> target = new HashMap<>();

        Map<String, List<String>> merged = Maps.mergeMap(source, target);
        assertSame("应返回源Map", source, merged);
        assertTrue("合并空Map应保持空", merged.isEmpty());
    }

    @Test
    public void testMapToObjWithNullMap() {
        // 测试null Map转对象
        Person person = Maps.mapToObj(null, Person.class);
        assertNull("null Map应转换为null对象", person);
    }
}
