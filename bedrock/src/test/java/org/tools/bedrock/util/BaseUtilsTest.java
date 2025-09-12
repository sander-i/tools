package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/20
 */
public class BaseUtilsTest {

    @Test
    public void testIsNull() {
        Address address = new Address();
        assertFalse("非null对象应该返回false", BaseUtils.isNull(address));
        address = null;
        assertTrue("null对象应该返回true", BaseUtils.isNull(address));
    }

    @Test
    public void testIsNotNull() {
        Address address = new Address();
        assertTrue("非null对象应该返回true", BaseUtils.isNotNull(address));
        address = null;
        assertFalse("null对象应该返回false", BaseUtils.isNotNull(address));
    }

    @Test
    public void testIsEmpty() {
        List<String> list = new ArrayList<>();
        list.add("1234567890");
        assertFalse("非空List应该返回false", BaseUtils.isEmpty(list));
        list.clear();
        assertTrue("空List应该返回true", BaseUtils.isEmpty(list));
        list = null;
        assertTrue("null List应该返回true", BaseUtils.isEmpty(list));
    }

    @Test
    public void testIsNotEmpty() {
        List<String> list = new ArrayList<>();
        list.add("1234567890");
        assertTrue("非空List应该返回true", BaseUtils.isNotEmpty(list));
        list.clear();
        assertFalse("空List应该返回false", BaseUtils.isNotEmpty(list));
        list = null;
        assertFalse("null List应该返回false", BaseUtils.isNotEmpty(list));
    }

    @Test
    public void testTestIsEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1234567890");
        assertFalse("非空Map应该返回false", BaseUtils.isEmpty(map));
        map.clear();
        assertTrue("空Map应该返回true", BaseUtils.isEmpty(map));
        map = null;
        assertTrue("null Map应该返回true", BaseUtils.isEmpty(map));
    }

    @Test
    public void testTestIsNotEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1234567890");
        assertTrue("非空Map应该返回true", BaseUtils.isNotEmpty(map));
        map.clear();
        assertFalse("空Map应该返回false", BaseUtils.isNotEmpty(map));
        map = null;
        assertFalse("null Map应该返回false", BaseUtils.isNotEmpty(map));
    }

    @Test
    public void testTestIsEmpty1() {
        Address address = new Address();
        assertFalse("非null对象应该返回false", BaseUtils.isEmpty(address));
        address = null;
        assertTrue("null对象应该返回true", BaseUtils.isEmpty(address));
    }

    @Test
    public void testTestIsNotEmpty1() {
        Address address = new Address();
        assertTrue("非null对象应该返回true", BaseUtils.isNotEmpty(address));
        address = null;
        assertFalse("null对象应该返回false", BaseUtils.isNotEmpty(address));
    }

    @Test
    public void testTestIsNull() {
        Address address = new Address();
        Address address1 = new Address();
        assertFalse("两个非null对象应该返回false", BaseUtils.isNull(address, address1));
        address = null;
        assertTrue("有一个null对象应该返回true", BaseUtils.isNull(address, address1));
    }

    @Test
    public void testIsAllNull() {
        Address address = new Address();
        Address address1 = new Address();
        assertFalse("两个非null对象应该返回false", BaseUtils.isAllNull(address, address1));
        address = null;
        assertFalse("只有一个null对象应该返回false", BaseUtils.isAllNull(address, address1));
        address1 = null;
        assertTrue("两个null对象应该返回true", BaseUtils.isAllNull(address, address1));
    }

    @Test
    public void testTestIsNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        assertTrue("两个非null对象应该返回true", BaseUtils.isNotNull(address, address1));
        address = null;
        assertFalse("有一个null对象应该返回false", BaseUtils.isNotNull(address, address1));
        address1 = null;
        assertFalse("两个null对象应该返回false", BaseUtils.isNotNull(address, address1));
    }

    @Test
    public void testIsAllNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        assertTrue("两个非null对象应该返回true", BaseUtils.isAllNotNull(address, address1));
        address = null;
        assertFalse("有一个null对象应该返回false", BaseUtils.isAllNotNull(address, address1));
        address1 = null;
        assertFalse("两个null对象应该返回false", BaseUtils.isAllNotNull(address, address1));
    }

    @Test
    public void testTestIsEmpty2() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        assertFalse("所有对象都非空应该返回false", BaseUtils.isEmpty(address, list, map, addresses));
        list = null;
        assertTrue("有一个对象为空应该返回true", BaseUtils.isEmpty(address, list, map, addresses));
    }

    @Test
    public void testIsAllEmpty() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        assertFalse("所有对象都非空应该返回false", BaseUtils.isAllEmpty(address, list, map, addresses));
        address = null;
        assertFalse("只有一个对象为空应该返回false", BaseUtils.isAllEmpty(address, list, map, addresses));
        addresses[0] = null;
        assertFalse("只有两个对象为空应该返回false", BaseUtils.isAllEmpty(address, list, map, addresses));
        list.clear();
        assertFalse("只有三个对象为空应该返回false", BaseUtils.isAllEmpty(address, list, map, addresses));
        map.clear();
        assertTrue("所有对象都为空应该返回true", BaseUtils.isAllEmpty(address, list, map, addresses));
    }

    @Test
    public void testTestIsNotEmpty2() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        assertTrue("所有对象都非空应该返回true", BaseUtils.isNotEmpty(address, list, map, addresses));
        address = null;
        assertTrue("有一个对象为空仍应返回true", BaseUtils.isNotEmpty(address, list, map, addresses));
        addresses[0] = null;
        assertTrue("有两个对象为空仍应返回true", BaseUtils.isNotEmpty(address, list, map, addresses));
        list.clear();
        assertTrue("有三个对象为空仍应返回true", BaseUtils.isNotEmpty(address, list, map, addresses));
        map.clear();
        assertFalse("所有对象都为空应该返回false", BaseUtils.isNotEmpty(address, list, map, addresses));
    }

    @Test
    public void testIsAllNotEmpty() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        assertTrue("所有对象都非空应该返回true", BaseUtils.isAllNotEmpty(address, list, map, addresses));
        list = null;
        assertFalse("有一个对象为空应该返回false", BaseUtils.isAllNotEmpty(address, list, map, addresses));
    }

    @Test
    public void testIsEmptyObj() {
        Address address = new Address();
        assertTrue("新创建的对象应该为空", BaseUtils.isEmptyObj(address));
        address.setCity("10010");
        assertFalse("设置了属性的对象应该不为空", BaseUtils.isEmptyObj(address));
    }
}
