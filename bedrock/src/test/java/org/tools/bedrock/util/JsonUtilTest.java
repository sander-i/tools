package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;

import java.util.ArrayList;
import java.util.List;

public class JsonUtilTest {

    @Test
    public void listToJson() {

        List<Address> addresses = new ArrayList<>();

        Address address = new Address("123 Main St", "Apt 45", "Apt 45", new Address.Area(), new Address.County(), "10001");

        addresses.add(address);

        String s = JsonUtil.listToJson(addresses);
        System.out.println(s);

    }

    @Test
    public void mapToJson() {
    }
}