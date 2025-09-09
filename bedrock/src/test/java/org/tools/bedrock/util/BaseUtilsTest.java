package org.tools.bedrock.util;

import junit.framework.TestCase;
import org.tools.bedrock.base.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/20
 */
public class BaseUtilsTest extends TestCase {

    public void testIsNull() {
        Address address = new Address();
        Aver.isFalse(BaseUtils.isNull(address), "非null对象应该返回false");
        address = null;
        Aver.isTrue(BaseUtils.isNull(address), "null对象应该返回true");
    }

    public void testIsNotNull() {
        Address address = new Address();
        Aver.isTrue(BaseUtils.isNotNull(address), "非null对象应该返回true");
        address = null;
        Aver.isFalse(BaseUtils.isNotNull(address), "null对象应该返回false");
    }

    public void testIsEmpty() {
        List<String> list = new ArrayList<>();
        list.add("1234567890");
        Aver.isFalse(BaseUtils.isEmpty(list), "非空List应该返回false");
        list.clear();
        Aver.isTrue(BaseUtils.isEmpty(list), "空List应该返回true");
        list = null;
        Aver.isTrue(BaseUtils.isEmpty(list), "null List应该返回true");
    }

    public void testIsNotEmpty() {
        List<String> list = new ArrayList<>();
        list.add("1234567890");
        Aver.isTrue(BaseUtils.isNotEmpty(list), "非空List应该返回true");
        list.clear();
        Aver.isFalse(BaseUtils.isNotEmpty(list), "空List应该返回false");
        list = null;
        Aver.isFalse(BaseUtils.isNotEmpty(list), "null List应该返回false");
    }

    public void testTestIsEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1234567890");
        Aver.isFalse(BaseUtils.isEmpty(map), "非空Map应该返回false");
        map.clear();
        Aver.isTrue(BaseUtils.isEmpty(map), "空Map应该返回true");
        map = null;
        Aver.isTrue(BaseUtils.isEmpty(map), "null Map应该返回true");
    }

    public void testTestIsNotEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1234567890");
        Aver.isTrue(BaseUtils.isNotEmpty(map), "非空Map应该返回true");
        map.clear();
        Aver.isFalse(BaseUtils.isNotEmpty(map), "空Map应该返回false");
        map = null;
        Aver.isFalse(BaseUtils.isNotEmpty(map), "null Map应该返回false");
    }

    public void testTestIsEmpty1() {
        Address address = new Address();
        Aver.isFalse(BaseUtils.isEmpty(address), "非null对象应该返回false");
        address = null;
        Aver.isTrue(BaseUtils.isEmpty(address), "null对象应该返回true");
    }

    public void testTestIsNotEmpty1() {
        Address address = new Address();
        Aver.isTrue(BaseUtils.isNotEmpty(address), "非null对象应该返回true");
        address = null;
        Aver.isFalse(BaseUtils.isNotEmpty(address), "null对象应该返回false");
    }

    public void testTestIsNull() {
        Address address = new Address();
        Address address1 = new Address();
        Aver.isFalse(BaseUtils.isNull(address, address1), "两个非null对象应该返回false");
        address = null;
        Aver.isTrue(BaseUtils.isNull(address, address1), "有一个null对象应该返回true");
    }

    public void testIsAllNull() {
        Address address = new Address();
        Address address1 = new Address();
        Aver.isFalse(BaseUtils.isAllNull(address, address1), "两个非null对象应该返回false");
        address = null;
        Aver.isFalse(BaseUtils.isAllNull(address, address1), "只有一个null对象应该返回false");
        address1 = null;
        Aver.isTrue(BaseUtils.isAllNull(address, address1), "两个null对象应该返回true");
    }

    public void testTestIsNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        Aver.isTrue(BaseUtils.isNotNull(address, address1), "两个非null对象应该返回true");
        address = null;
        Aver.isFalse(BaseUtils.isNotNull(address, address1), "有一个null对象应该返回false");
        address1 = null;
        Aver.isFalse(BaseUtils.isNotNull(address, address1), "两个null对象应该返回false");
    }

    public void testIsAllNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        Aver.isTrue(BaseUtils.isAllNotNull(address, address1), "两个非null对象应该返回true");
        address = null;
        Aver.isFalse(BaseUtils.isAllNotNull(address, address1), "有一个null对象应该返回false");
        address1 = null;
        Aver.isFalse(BaseUtils.isAllNotNull(address, address1), "两个null对象应该返回false");
    }

    public void testTestIsEmpty2() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        Aver.isFalse(BaseUtils.isEmpty(address, list, map, addresses), "所有对象都非空应该返回false");
        list = null;
        Aver.isTrue(BaseUtils.isEmpty(address, list, map, addresses), "有一个对象为空应该返回true");
    }

    public void testIsAllEmpty() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        Aver.isFalse(BaseUtils.isAllEmpty(address, list, map, addresses), "所有对象都非空应该返回false");
        address = null;
        Aver.isFalse(BaseUtils.isAllEmpty(address, list, map, addresses), "只有一个对象为空应该返回false");
        addresses[0] = null;
        Aver.isFalse(BaseUtils.isAllEmpty(address, list, map, addresses), "只有两个对象为空应该返回false");
        list.clear();
        Aver.isFalse(BaseUtils.isAllEmpty(address, list, map, addresses), "只有三个对象为空应该返回false");
        map.clear();
        Aver.isTrue(BaseUtils.isAllEmpty(address, list, map, addresses), "所有对象都为空应该返回true");
    }

    public void testTestIsNotEmpty2() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        Aver.isTrue(BaseUtils.isNotEmpty(address, list, map, addresses), "所有对象都非空应该返回true");
        address = null;
        Aver.isTrue(BaseUtils.isNotEmpty(address, list, map, addresses), "有一个对象为空仍应返回true");
        addresses[0] = null;
        Aver.isTrue(BaseUtils.isNotEmpty(address, list, map, addresses), "有两个对象为空仍应返回true");
        list.clear();
        Aver.isTrue(BaseUtils.isNotEmpty(address, list, map, addresses), "有三个对象为空仍应返回true");
        map.clear();
        Aver.isFalse(BaseUtils.isNotEmpty(address, list, map, addresses), "所有对象都为空应该返回false");
    }

    public void testIsAllNotEmpty() {
        Address address = new Address();
        List<List<?>> list = new ArrayList<>();
        list.add(list);
        Map<String, List<?>> map = new HashMap<>();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        Aver.isTrue(BaseUtils.isAllNotEmpty(address, list, map, addresses), "所有对象都非空应该返回true");
        list = null;
        Aver.isFalse(BaseUtils.isAllNotEmpty(address, list, map, addresses), "有一个对象为空应该返回false");
    }

    public void testIsEmptyObj() {
        Address address = new Address();
        Aver.isTrue(BaseUtils.isEmptyObj(address), "新创建的对象应该为空");
        address.setCity("10010");
        Aver.isFalse(BaseUtils.isEmptyObj(address), "设置了属性的对象应该不为空");
    }
}
