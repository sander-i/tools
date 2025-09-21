package org.tools.bedrock.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class CopyUtilTest {

    // 测试用的源对象类
    static class SourceEntity {
        private String name;
        private int age;
        private boolean active;
        private BigDecimal amount;
        private List<String> tags;
        private InnerEntity inner;

        public SourceEntity() {
        }

        public SourceEntity(String name, int age, boolean active, BigDecimal amount) {
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

        public InnerEntity getInner() {
            return inner;
        }

        public void setInner(InnerEntity inner) {
            this.inner = inner;
        }
    }

    // 测试用的目标对象类
    static class TargetEntity {
        private String name;
        private int age;
        private boolean active;
        private BigDecimal amount;
        private List<String> tags;
        private InnerEntity inner;

        public TargetEntity() {
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

        public InnerEntity getInner() {
            return inner;
        }

        public void setInner(InnerEntity inner) {
            this.inner = inner;
        }
    }

    // 内部嵌套对象类
    static class InnerEntity {
        private String description;
        private int code;

        public InnerEntity() {
        }

        public InnerEntity(String description, int code) {
            this.description = description;
            this.code = code;
        }

        // Getters and Setters
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    // 带继承关系的源对象类
    static class ExtendedSourceEntity extends SourceEntity {
        private String extraField;

        public ExtendedSourceEntity() {
        }

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
        }
    }

    // 带继承关系的目标对象类
    static class ExtendedTargetEntity extends TargetEntity {
        private String extraField;

        public ExtendedTargetEntity() {
        }

        public String getExtraField() {
            return extraField;
        }

        public void setExtraField(String extraField) {
            this.extraField = extraField;
        }
    }

    @Test
    public void testCopyObjBasic() {
        // 测试基本对象复制功能
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        TargetEntity target = new TargetEntity();

        CopyUtil.copyObj(source, target);

        assertEquals("test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(true, target.isActive());
        assertEquals(new BigDecimal("100.50"), target.getAmount());
    }

    @Test
    public void testCopyObjWithClass() {
        // 测试通过类信息创建目标对象并复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));

        TargetEntity target = CopyUtil.copyObj(source, TargetEntity.class);

        assertNotNull(target);
        assertEquals("test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(true, target.isActive());
        assertEquals(new BigDecimal("100.50"), target.getAmount());
    }

    @Test
    public void testCopyObjWithIgnoreProperties() {
        // 测试忽略属性的对象复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        TargetEntity target = new TargetEntity();

        CopyUtil.copyObj(source, target, "age", "amount");

        assertEquals("test", target.getName());
        assertEquals(true, target.isActive());
        // 被忽略的属性应该保持默认值
        assertEquals(0, target.getAge());
        assertNull(target.getAmount());
    }

    @Test
    public void testCopyObjWithNestedObject() {
        // 测试嵌套对象复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        InnerEntity inner = new InnerEntity("description", 123);
        source.setInner(inner);

        TargetEntity target = new TargetEntity();

        CopyUtil.copyObj(source, target);

        assertEquals("test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(true, target.isActive());
        assertEquals(new BigDecimal("100.50"), target.getAmount());

        assertNotNull(target.getInner());
        assertEquals("description", target.getInner().getDescription());
        assertEquals(123, target.getInner().getCode());
    }

    @Test
    public void testCopyCurrentObj() {
        // 测试仅复制当前对象属性（不包括父类属性）
        ExtendedSourceEntity source = new ExtendedSourceEntity();
        source.setName("test");
        source.setAge(25);
        source.setExtraField("extra");

        ExtendedTargetEntity target = new ExtendedTargetEntity();

        CopyUtil.copyCurrentObj(source, target);

        assertNotEquals("test", target.getName());
        assertNotEquals(25, target.getAge());
        assertEquals("extra", target.getExtraField());
    }

    @Test
    public void testCopyCurrentObjWithClass() {
        // 测试通过类信息创建目标对象并仅复制当前对象属性
        ExtendedSourceEntity source = new ExtendedSourceEntity();
        source.setName("test");
        source.setAge(25);
        source.setExtraField("extra");

        ExtendedTargetEntity target = CopyUtil.copyCurrentObj(source, ExtendedTargetEntity.class);

        assertNotNull(target);
        assertNotEquals("test", target.getName());
        assertNotEquals(25, target.getAge());
        assertEquals("extra", target.getExtraField());
    }

    @Test
    public void testCopyObjWithCollection() {
        // 测试集合类型属性复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        source.setTags(tags);

        TargetEntity target = new TargetEntity();

        CopyUtil.copyObj(source, target);

        assertEquals("test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(true, target.isActive());
        assertEquals(new BigDecimal("100.50"), target.getAmount());

        assertNotNull(target.getTags());
        assertEquals(2, target.getTags().size());
        assertTrue(target.getTags().contains("tag1"));
        assertTrue(target.getTags().contains("tag2"));
    }

    @Test
    public void testCopyObjWithMap() {
        // 测试Map类型属性复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        java.util.Map<String, String> properties = new java.util.HashMap<>();
        properties.put("key1", "value1");
        properties.put("key2", "value2");
        source.setInner(new InnerEntity()); // 先设置一个inner对象，然后通过反射设置map属性
        // 由于SourceEntity中没有直接的Map属性，我们创建一个新的测试类来验证Map复制
        MapSourceEntity mapSource = new MapSourceEntity();
        mapSource.setName("mapTest");
        mapSource.setProperties(properties);

        MapTargetEntity mapTarget = new MapTargetEntity();

        CopyUtil.copyObj(mapSource, mapTarget);

        assertEquals("mapTest", mapTarget.getName());
        assertNotNull(mapTarget.getProperties());
        assertEquals(2, mapTarget.getProperties().size());
        assertEquals("value1", mapTarget.getProperties().get("key1"));
        assertEquals("value2", mapTarget.getProperties().get("key2"));
    }

    @Test
    public void testCopyObjWithSet() {
        // 测试Set类型属性复制
        SourceEntity source = new SourceEntity("test", 25, true, new BigDecimal("100.50"));
        java.util.Set<String> tagSet = new java.util.HashSet<>();
        tagSet.add("tagA");
        tagSet.add("tagB");
        // 同样，SourceEntity中没有直接的Set属性，创建新的测试类
        SetSourceEntity setSource = new SetSourceEntity();
        setSource.setName("setTest");
        setSource.setTagSet(tagSet);

        SetTargetEntity setTarget = new SetTargetEntity();

        CopyUtil.copyObj(setSource, setTarget);

        assertEquals("setTest", setTarget.getName());
        assertNotNull(setTarget.getTagSet());
        assertEquals(2, setTarget.getTagSet().size());
        assertTrue(setTarget.getTagSet().contains("tagA"));
        assertTrue(setTarget.getTagSet().contains("tagB"));
    }

    // 为Map测试新增的类
    static class MapSourceEntity {
        private String name;
        private java.util.Map<String, String> properties;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.util.Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(java.util.Map<String, String> properties) {
            this.properties = properties;
        }
    }

    static class MapTargetEntity {
        private String name;
        private java.util.Map<String, String> properties;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.util.Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(java.util.Map<String, String> properties) {
            this.properties = properties;
        }
    }

    // 为Set测试新增的类
    static class SetSourceEntity {
        private String name;
        private java.util.Set<String> tagSet;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.util.Set<String> getTagSet() {
            return tagSet;
        }

        public void setTagSet(java.util.Set<String> tagSet) {
            this.tagSet = tagSet;
        }
    }

    static class SetTargetEntity {
        private String name;
        private java.util.Set<String> tagSet;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.util.Set<String> getTagSet() {
            return tagSet;
        }

        public void setTagSet(java.util.Set<String> tagSet) {
            this.tagSet = tagSet;
        }
    }

    @Test
    public void testCopyObjWithNullValues() {
        // 测试处理null值的情况
        SourceEntity source = new SourceEntity();
        source.setName(null);
        source.setAge(25);
        source.setAmount(null);

        TargetEntity target = new TargetEntity();
        target.setName("original");

        CopyUtil.copyObj(source, target);

        // null值不应该覆盖已有值
        assertNotNull(target.getName());
        assertEquals(25, target.getAge());
        assertNull(target.getAmount());
    }
}