package org.tools.bedrock.util;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClassUtilsTest extends TestCase {

    // 测试用的示例类
    static class TestEntity {
        private String name;
        private int age;
        private boolean active;
        private BigDecimal amount;
        private List<String> tags;

        public TestEntity() {
        }

        public TestEntity(String name, int age, boolean active, BigDecimal amount) {
            this.name = name;
            this.age = age;
            this.active = active;
            this.amount = amount;
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

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    // 带继承关系的测试类
    static class ExtendedTestEntity extends TestEntity {
        private String extraField;

        public ExtendedTestEntity() {
        }

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
        }
    }

    public void testIsBaseClass() {
        // 测试基础类型检查
        assertTrue(ClassUtils.isBaseClass("int"));
        assertTrue(ClassUtils.isBaseClass("double"));
        assertTrue(ClassUtils.isBaseClass("boolean"));
        assertFalse(ClassUtils.isBaseClass("String"));
        assertFalse(ClassUtils.isBaseClass("BigDecimal"));
    }

    public void testNewClass() {
        try {
            // 测试类实例化
            Class<?> stringClass = ClassUtils.newClass("java.lang.String");
            assertEquals(String.class, stringClass);

            Class<?> integerClass = ClassUtils.newClass("java.lang.Integer");
            assertEquals(Integer.class, integerClass);
        } catch (ClassNotFoundException e) {
            fail("不应该抛出ClassNotFoundException: " + e.getMessage());
        }
    }

    public void testNewInstance() {
        try {
            // 测试对象实例化
            Object stringInstance = ClassUtils.newInstance("java.lang.String");
            assertTrue(stringInstance instanceof String);

            Object arrayListInstance = ClassUtils.newInstance("java.util.ArrayList");
            assertTrue(arrayListInstance instanceof ArrayList);
        } catch (Exception e) {
            fail("不应该抛出异常: " + e.getMessage());
        }
    }

    public void testGetDeclaredFields() {
        try {
            // 测试获取类字段
            Field[] fields = ClassUtils.getDeclaredFields(TestEntity.class);
            assertEquals(5, fields.length); // name, age, active, amount, tags

            // 验证字段名称
            boolean nameFound = false;
            boolean ageFound = false;
            boolean activeFound = false;
            boolean amountFound = false;
            boolean tagsFound = false;

            for (Field field : fields) {
                switch (field.getName()) {
                    case "name":
                        nameFound = true;
                        break;
                    case "age":
                        ageFound = true;
                        break;
                    case "active":
                        activeFound = true;
                        break;
                    case "amount":
                        amountFound = true;
                        break;
                    case "tags":
                        tagsFound = true;
                        break;
                }
            }

            assertTrue(nameFound);
            assertTrue(ageFound);
            assertTrue(activeFound);
            assertTrue(amountFound);
            assertTrue(tagsFound);
        } catch (Exception e) {
            fail("不应该抛出异常: " + e.getMessage());
        }
    }

    public void testGetAllDeclaredFields() {
        try {
            // 测试获取类及其父类的所有字段
            Field[] fields = ClassUtils.getAllDeclaredFields(ExtendedTestEntity.class);
            // 应该包含TestEntity的5个字段和ExtendedTestEntity的1个字段
            assertTrue(fields.length >= 6);
        } catch (Exception e) {
            fail("不应该抛出异常: " + e.getMessage());
        }
    }

    public void testGetProperty() {
        // 测试属性读取
        TestEntity entity = new TestEntity("test", 25, true, new BigDecimal("100.50"));

        Object name = ClassUtils.getProperty(entity, "name");
        assertEquals("test", name);

        Object age = ClassUtils.getProperty(entity, "age");
        assertEquals(25, age);

        Object active = ClassUtils.getProperty(entity, "active");
        assertEquals(true, active);
    }

    public void testSetProperty() {
        // 测试属性写入
        TestEntity entity = new TestEntity();

        boolean nameResult = ClassUtils.setProperty(entity, "name", "newName");
        assertTrue(nameResult);
        assertEquals("newName", entity.getName());

        boolean ageResult = ClassUtils.setProperty(entity, "age", 30);
        assertTrue(ageResult);
        assertEquals(30, entity.getAge());
    }

    public void testStrSeparatorFirstCapitalize() {
        // 测试字符串分隔符首字母大写转换
        String result1 = ClassUtils.strSeparatorFirstCapitalize("str_separator_first_capitalize");
        assertEquals("strSeparatorFirstCapitalize", result1);

        String result2 = ClassUtils.strSeparatorFirstCapitalize("user_name");
        assertEquals("userName", result2);

        String result3 = ClassUtils.strSeparatorFirstCapitalize("first_name_test");
        assertEquals("firstNameTest", result3);
    }

    public void testHandleBooleanType() throws NoSuchFieldException {
        // 测试布尔类型处理
        Field activeField = TestEntity.class.getDeclaredField("active");
        String fieldName = ClassUtils.handleBooleanType(activeField);
        assertEquals("active", fieldName); // 不应该改变非is前缀的布尔字段

        // 创建一个带is前缀的布尔字段测试类
        class TestBooleanEntity {
            private boolean isActive;
            private boolean valid;

            public boolean isActive() {
                return isActive;
            }

            public boolean isValid() {
                return valid;
            }
        }

        Field isActiveField = TestBooleanEntity.class.getDeclaredField("isActive");
        String isActiveFieldName = ClassUtils.handleBooleanType(isActiveField);
        assertEquals("Active", isActiveFieldName); // 去掉is前缀，首字母大写
    }

    public void testCommonFields() {
        // 测试获取两个类的公共字段
        Field[] commonFields = ClassUtils.commonFields(TestEntity.class, ExtendedTestEntity.class, false);
        // ExtendedTestEntity继承自TestEntity，继承对象不含父类的字段，所以公共字段数量小于2
        assertTrue(commonFields.length < 2);
    }

    public void testAllCommonFields() {
        // 测试获取两个类的公共字段
        Field[] commonFields = ClassUtils.allCommonFields(TestEntity.class, ExtendedTestEntity.class, false);
        // ExtendedTestEntity继承自TestEntity，继承对象包含父类的字段，所以公共字段数量大于等于5
        assertTrue(commonFields.length >= 5);
    }

    public void testIsEnumClass() {
        // 测试枚举类检查
        assertFalse(ClassUtils.isEnumClass(null));
        assertFalse(ClassUtils.isEnumClass("not a class"));
        assertFalse(ClassUtils.isEnumClass(TestEntity.class));
        // 如果有枚举类可以测试，可以添加相应的测试
    }

    public void testNativeClass() {
        // 测试原生类检查
        assertFalse(ClassUtils.nativeClass(null));
        assertTrue(ClassUtils.nativeClass("string"));
        assertTrue(ClassUtils.nativeClass(123));
        assertTrue(ClassUtils.nativeClass(new BigDecimal("100")));
    }
}
