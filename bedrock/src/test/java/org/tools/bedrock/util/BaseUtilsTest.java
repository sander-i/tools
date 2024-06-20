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
        boolean isNull = BaseUtils.isNull(address);
        System.out.println("isNull " + isNull + " address: " + address);
        address = null;
        isNull = BaseUtils.isNull(address);
        System.out.println("isNull " + isNull + " address: " + address);
    }

    public void testIsNotNull() {
        Address address = new Address();
        boolean isNotNull = BaseUtils.isNotNull(address);
        System.out.println("isNotNull " + isNotNull + " address: " + address);
        address = null;
        isNotNull = BaseUtils.isNotNull(address);
        System.out.println("isNotNull " + isNotNull + " address: " + address);
    }

    public void testIsEmpty() {
        List list = new ArrayList();
        list.add("1234567890");
        boolean isEmpty = BaseUtils.isEmpty(list);
        System.out.println("isEmpty " + isEmpty + " list: " + list);
        list.clear();
        isEmpty = BaseUtils.isEmpty(list);
        System.out.println("isEmpty " + isEmpty + " list: " + list);
        list = null;
        isEmpty = BaseUtils.isEmpty(list);
        System.out.println("isEmpty " + isEmpty + " list: " + list);
    }

    public void testIsNotEmpty() {
        List list = new ArrayList();
        list.add("1234567890");
        boolean isNotEmpty = BaseUtils.isNotEmpty(list);
        System.out.println("isNotEmpty " + isNotEmpty + " list: " + list);
        list.clear();
        isNotEmpty = BaseUtils.isNotEmpty(list);
        System.out.println("isNotEmpty " + isNotEmpty + " list: " + list);
        list = null;
        isNotEmpty = BaseUtils.isNotEmpty(list);
        System.out.println("isNotEmpty " + isNotEmpty + " list: " + list);
    }

    public void testTestIsEmpty() {
        Map map = new HashMap<>();
        map.put("1", "1234567890");
        boolean isEmpty = BaseUtils.isEmpty(map);
        System.out.println("isEmpty " + isEmpty + " map: " + map);
        map.clear();
        isEmpty = BaseUtils.isEmpty(map);
        System.out.println("isEmpty " + isEmpty + " map: " + map);
        map = null;
        isEmpty = BaseUtils.isEmpty(map);
        System.out.println("isEmpty " + isEmpty + " map: " + map);
    }

    public void testTestIsNotEmpty() {
        Map map = new HashMap<>();
        map.put("1", "1234567890");
        boolean isNotEmpty = BaseUtils.isNotEmpty(map);
        System.out.println("isNotEmpty " + isNotEmpty + " map: " + map);
        map.clear();
        isNotEmpty = BaseUtils.isNotEmpty(map);
        System.out.println("isNotEmpty " + isNotEmpty + " map: " + map);
        map = null;
        isNotEmpty = BaseUtils.isNotEmpty(map);
        System.out.println("isNotEmpty " + isNotEmpty + " map: " + map);
    }

    public void testTestIsEmpty1() {
        Address address = new Address();
        boolean isEmpty = BaseUtils.isEmpty(address);
        System.out.println("isEmpty " + isEmpty + " address: " + address);
        address = null;
        isEmpty = BaseUtils.isEmpty(address);
        System.out.println("isEmpty " + isEmpty + " address: " + address);
    }

    public void testTestIsNotEmpty1() {
        Address address = new Address();
        boolean isNotEmpty = BaseUtils.isNotEmpty(address);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address);
        address = null;
        isNotEmpty = BaseUtils.isNotEmpty(address);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address);
    }

    public void testTestIsNull() {
        Address address = new Address();
        Address address1 = new Address();
        boolean isNull = BaseUtils.isNull(address, address1);
        System.out.println("isNull " + isNull + " address: " + address + " address1: " + address1);
        address = null;
        isNull = BaseUtils.isNull(address, address1);
        System.out.println("isNull " + isNull + " address: " + address + " address1: " + address1);
    }

    public void testIsAllNull() {
        Address address = new Address();
        Address address1 = new Address();
        boolean isAllNull = BaseUtils.isAllNull(address, address1);
        System.out.println("isAllNull " + isAllNull + " address: " + address + " address1: " + address1);
        address = null;
        isAllNull = BaseUtils.isAllNull(address, address1);
        System.out.println("isAllNull " + isAllNull + " address: " + address + " address1: " + address1);
        address1 = null;
        isAllNull = BaseUtils.isAllNull(address, address1);
        System.out.println("isAllNull " + isAllNull + " address: " + address + " address1: " + address1);
    }

    public void testTestIsNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        boolean isNotNull = BaseUtils.isNotNull(address, address1);
        System.out.println("isNotNull " + isNotNull + " address: " + address + " address1: " + address1);
        address = null;
        isNotNull = BaseUtils.isNotNull(address, address1);
        System.out.println("isNotNull " + isNotNull + " address: " + address + " address1: " + address1);
        address1 = null;
        isNotNull = BaseUtils.isNotNull(address, address1);
        System.out.println("isNotNull " + isNotNull + " address: " + address + " address1: " + address1);
    }

    public void testIsAllNotNull() {
        Address address = new Address();
        Address address1 = new Address();
        boolean isAllNotNull = BaseUtils.isAllNotNull(address, address1);
        System.out.println("isAllNotNull " + isAllNotNull + " address: " + address + " address1: " + address1);
        address = null;
        isAllNotNull = BaseUtils.isAllNotNull(address, address1);
        System.out.println("isAllNotNull " + isAllNotNull + " address: " + address + " address1: " + address1);
        address1 = null;
        isAllNotNull = BaseUtils.isAllNotNull(address, address1);
        System.out.println("isAllNotNull " + isAllNotNull + " address: " + address + " address1: " + address1);
    }

    public void testTestIsEmpty2() {
        Address address = new Address();
        List list = new ArrayList();
        list.add(list);
        Map map = new HashMap();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        boolean isEmpty = BaseUtils.isEmpty(address, list, map, addresses);
        System.out.println("isEmpty " + isEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        list = null;
        isEmpty = BaseUtils.isEmpty(address, list, map, addresses);
        System.out.println("isEmpty " + isEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
    }

    public void testIsAllEmpty() {
        Address address = new Address();
        List list = new ArrayList();
        list.add(list);
        Map map = new HashMap();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        boolean isAllEmpty = BaseUtils.isAllEmpty(address, list, map, addresses);
        System.out.println("isAllEmpty " + isAllEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        address = null;
        isAllEmpty = BaseUtils.isAllEmpty(address, list, map, addresses);
        System.out.println("isAllEmpty " + isAllEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        addresses[0] = null;
        isAllEmpty = BaseUtils.isAllEmpty(address, list, map, addresses);
        System.out.println("isAllEmpty " + isAllEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        list.clear();
        isAllEmpty = BaseUtils.isAllEmpty(address, list, map, addresses);
        System.out.println("isAllEmpty " + isAllEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        map.clear();
        isAllEmpty = BaseUtils.isAllEmpty(address, list, map, addresses);
        System.out.println("isAllEmpty " + isAllEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
    }

    public void testTestIsNotEmpty2() {
        Address address = new Address();
        List list = new ArrayList();
        list.add(list);
        Map map = new HashMap();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        boolean isNotEmpty = BaseUtils.isNotEmpty(address, list, map, addresses);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        address = null;
        isNotEmpty = BaseUtils.isNotEmpty(address, list, map, addresses);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        addresses[0] = null;
        isNotEmpty = BaseUtils.isNotEmpty(address, list, map, addresses);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        list.clear();
        isNotEmpty = BaseUtils.isNotEmpty(address, list, map, addresses);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        map.clear();
        isNotEmpty = BaseUtils.isNotEmpty(address, list, map, addresses);
        System.out.println("isNotEmpty " + isNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
    }

    public void testIsAllNotEmpty() {
        Address address = new Address();
        List list = new ArrayList();
        list.add(list);
        Map map = new HashMap();
        map.put("1", list);
        Address[] addresses = new Address[1];
        addresses[0] = address;
        boolean isAllNotEmpty = BaseUtils.isAllNotEmpty(address, list, map, addresses);
        System.out.println("isAllNotEmpty " + isAllNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
        list = null;
        isAllNotEmpty = BaseUtils.isAllNotEmpty(address, list, map, addresses);
        System.out.println("isAllNotEmpty " + isAllNotEmpty + " address: " + address + " list: " + list + " map: " + map + " addresses: " + addresses);
    }

    public void testIsEmptyObj() {
        Address address = new Address();
        boolean isEmptyObj = BaseUtils.isEmptyObj(address);
        System.out.println("isEmptyObj " + isEmptyObj + " address: " + address);
        address.setCity("10010");
        isEmptyObj = BaseUtils.isEmptyObj(address);
        System.out.println("isEmptyObj " + isEmptyObj + " address: " + address);
    }
}