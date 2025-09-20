package org.tools.fortify.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * JSONUtils 冒烟测试
 * 测试基本的JSON转换功能
 */
public class JSONUtilsTest {

    // 测试用的简单POJO类
    public static class Person {
        private String name;
        private int age;
        private boolean active;
        private Date birthDate;
        private List<String> hobbies;
        private Map<String, String> attributes;
        private StringBuffer notes;
        private StringBuilder description;
        private Address address;

        public Person() {
        }

        public Person(String name, int age, boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }

        // Getters and Setters
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

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        public List<String> getHobbies() {
            return hobbies;
        }

        public void setHobbies(List<String> hobbies) {
            this.hobbies = hobbies;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public StringBuffer getNotes() {
            return notes;
        }

        public void setNotes(StringBuffer notes) {
            this.notes = notes;
        }

        public StringBuilder getDescription() {
            return description;
        }

        public void setDescription(StringBuilder description) {
            this.description = description;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age &&
                    active == person.active &&
                    Objects.equals(name, person.name) &&
                    Objects.equals(birthDate, person.birthDate) &&
                    Objects.equals(hobbies, person.hobbies) &&
                    Objects.equals(attributes, person.attributes) &&
                    Objects.equals(notes, person.notes) &&
                    Objects.equals(description, person.description) &&
                    Objects.equals(address, person.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age, active, birthDate, hobbies, attributes, notes, description, address);
        }
    }

    // 嵌套测试类
    public static class Address {
        private String street;
        private String city;

        public Address() {
        }

        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }

        // Getters and Setters
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Address address = (Address) o;
            return Objects.equals(street, address.street) &&
                    Objects.equals(city, address.city);
        }

        @Override
        public int hashCode() {
            return Objects.hash(street, city);
        }
    }

    @Test
    public void testObjToJson() {
        // 测试对象转JSON字符串
        Person person = new Person("张三", 25, true);
        String json = JSONUtils.objToJson(person);

        assertNotNull("转换后的JSON字符串不应为null", json);
        assertFalse("转换后的JSON字符串不应为空", json.isEmpty());
        assertTrue("JSON字符串应包含name属性", json.contains("\"name\":\"张三\""));
        assertTrue("JSON字符串应包含age属性", json.contains("\"age\":25"));
        assertTrue("JSON字符串应包含active属性", json.contains("\"active\":true"));
    }

    @Test
    public void testObjToJsonWithNull() {
        // 测试null对象转JSON字符串
        String json = JSONUtils.objToJson(null);
        assertEquals("null对象应转换为空字符串", "", json);
    }

    @Test
    public void testJsonToObj() {
        // 测试JSON字符串转JSONObject
        String jsonStr = "{\"name\":\"李四\",\"age\":30,\"active\":false}";
        JSONObject jsonObject = JSONUtils.jsonToObj(jsonStr);

        assertNotNull("转换后的JSONObject不应为null", jsonObject);
        assertEquals("name属性应正确设置", "李四", jsonObject.getString("name"));
        assertEquals("age属性应正确设置", 30, jsonObject.getIntValue("age"));
        assertEquals("active属性应正确设置", false, jsonObject.getBooleanValue("active"));
    }

    @Test
    public void testJsonToObjWithNull() {
        // 测试null JSON字符串转JSONObject
        JSONObject jsonObject = JSONUtils.jsonToObj(null);
        assertNotNull("转换后的JSONObject不应为null", jsonObject);
    }

    @Test
    public void testJsonToObjWithEmptyString() {
        // 测试空JSON字符串转JSONObject
        JSONObject jsonObject = JSONUtils.jsonToObj("");
        assertNotNull("转换后的JSONObject不应为null", jsonObject);
    }

    @Test
    public void testJsonToObjWithClass() {
        // 测试JSON字符串转指定类型对象
        String jsonStr = "{\"name\":\"王五\",\"age\":35,\"active\":true}";
        Person person = (Person) JSONUtils.jsonToObj(jsonStr, Person.class);

        assertNotNull("转换后的对象不应为null", person);
        assertEquals("name属性应正确设置", "王五", person.getName());
        assertEquals("age属性应正确设置", 35, person.getAge());
        assertEquals("active属性应正确设置", true, person.isActive());
    }

    @Test
    public void testJsonToObjWithClassAndNull() {
        // 测试null JSON字符串转指定类型对象
        Object obj = JSONUtils.jsonToObj(null, Person.class);
        assertNotNull("转换后的对象不应为null", obj);
        assertTrue("应返回JSONObject实例", obj instanceof JSONObject);
    }

    @Test
    public void testObjToJsonArray() {
        // 测试对象转JSON数组
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("赵六", 28, true));
        persons.add(new Person("孙七", 32, false));

        List<Person> result = JSONUtils.objToJsonArray(persons, Person.class);

        assertNotNull("转换后的列表不应为null", result);
        assertEquals("列表大小应为2", 2, result.size());
        assertEquals("第一个元素name应正确", "赵六", result.get(0).getName());
        assertEquals("第二个元素name应正确", "孙七", result.get(1).getName());
    }

    @Test
    public void testObjToJsonArrayWithNull() {
        // 测试null对象转JSON数组
        List<Person> result = JSONUtils.objToJsonArray(null, Person.class);
        assertNotNull("转换后的列表不应为null", result);
        assertTrue("应返回空列表", result.isEmpty());
    }

    @Test
    public void testJsonToObjWithComplexObject() throws Exception {
        // 测试复杂对象的JSON转换
        String jsonStr = "{\n" +
                "  \"name\": \"复杂对象\",\n" +
                "  \"age\": 40,\n" +
                "  \"active\": true,\n" +
                "  \"birthDate\": 1609459200000,\n" +
                "  \"hobbies\": [\"读书\", \"游泳\"],\n" +
                "  \"attributes\": {\"height\": \"175cm\", \"weight\": \"70kg\"},\n" +
                "  \"notes\": \"测试笔记\",\n" +
                "  \"description\": \"测试描述\",\n" +
                "  \"address\": {\n" +
                "    \"street\": \"测试街道\",\n" +
                "    \"city\": \"测试城市\"\n" +
                "  }\n" +
                "}";

        Person person = new Person();
        Person result = (Person) JSONUtils.jsonToObj(jsonStr, person);

        assertSame("应返回相同的对象实例", person, result);
        assertEquals("name属性应正确设置", "复杂对象", person.getName());
        assertEquals("age属性应正确设置", 40, person.getAge());
        assertEquals("active属性应正确设置", true, person.isActive());
        assertNotNull("birthDate属性应正确设置", person.getBirthDate());
        assertEquals("hobbies属性应正确设置", 2, person.getHobbies().size());
        assertTrue("hobbies应包含读书", person.getHobbies().contains("读书"));
        assertTrue("hobbies应包含游泳", person.getHobbies().contains("游泳"));
        assertEquals("attributes属性应正确设置", 2, person.getAttributes().size());
        assertEquals("attributes.height应正确设置", "175cm", person.getAttributes().get("height"));
        assertEquals("attributes.weight应正确设置", "70kg", person.getAttributes().get("weight"));
        assertEquals("notes属性应正确设置", "测试笔记", person.getNotes().toString());
        assertEquals("description属性应正确设置", "测试描述", person.getDescription().toString());
        assertNotNull("address属性应正确设置", person.getAddress());
        assertEquals("address.street应正确设置", "测试街道", person.getAddress().getStreet());
        assertEquals("address.city应正确设置", "测试城市", person.getAddress().getCity());
    }

    @Test
    public void testJsonToObjWithStringDate() throws Exception {
        // 测试字符串日期转换
        String jsonStr = "{\n" +
                "  \"name\": \"日期测试\",\n" +
                "  \"age\": 25,\n" +
                "  \"active\": true,\n" +
                "  \"birthDate\": \"2021-01-01 00:00:00\"\n" +
                "}";

        Person person = new Person();
        Person result = (Person) JSONUtils.jsonToObj(jsonStr, person);

        assertSame("应返回相同的对象实例", person, result);
        assertEquals("name属性应正确设置", "日期测试", person.getName());
        assertNotNull("birthDate属性应正确设置", person.getBirthDate());
    }

    @Test
    public void testJsonToObjWithEmptyValues() throws Exception {
        // 测试空值处理
        String jsonStr = "{\n" +
                "  \"name\": \"空值测试\",\n" +
                "  \"age\": 25,\n" +
                "  \"active\": true,\n" +
                "  \"hobbies\": [],\n" +
                "  \"attributes\": {}\n" +
                "}";

        Person person = new Person();
        Person result = (Person) JSONUtils.jsonToObj(jsonStr, person);

        assertSame("应返回相同的对象实例", person, result);
        assertEquals("name属性应正确设置", "空值测试", person.getName());
        assertNotNull("hobbies属性应正确设置", person.getHobbies());
        assertTrue("hobbies应为空列表", person.getHobbies().isEmpty());
        assertNotNull("attributes属性应正确设置", person.getAttributes());
        assertTrue("attributes应为空Map", person.getAttributes().isEmpty());
    }

    @Test
    public void testRoundTripConversion() throws Exception {
        // 测试往返转换：对象->JSON->对象
        Person original = new Person("往返测试", 30, true);
        original.setHobbies(Arrays.asList("跑步", "爬山"));
        original.setAttributes(new HashMap<String, String>() {{
            put("skill", "Java");
            put("level", "Senior");
        }});
        original.setNotes(new StringBuffer("测试笔记"));
        original.setDescription(new StringBuilder("测试描述"));
        original.setAddress(new Address("测试街道123号", "测试城市"));

        String json = JSONUtils.objToJson(original);
        Person person = new Person();
        Person result = (Person) JSONUtils.jsonToObj(json, person);

        assertSame("应返回相同的对象实例", person, result);
        assertEquals("name属性应保持一致", original.getName(), person.getName());
        assertEquals("age属性应保持一致", original.getAge(), person.getAge());
        assertEquals("active属性应保持一致", original.isActive(), person.isActive());
        assertEquals("hobbies属性应保持一致", original.getHobbies(), person.getHobbies());
        assertEquals("attributes属性应保持一致", original.getAttributes(), person.getAttributes());
        assertEquals("notes属性应保持一致", original.getNotes().toString(), person.getNotes().toString());
        assertEquals("description属性应保持一致", original.getDescription().toString(), person.getDescription().toString());
        assertEquals("address属性应保持一致", original.getAddress(), person.getAddress());
    }

}
