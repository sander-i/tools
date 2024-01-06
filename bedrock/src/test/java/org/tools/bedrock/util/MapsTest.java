package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;
import org.tools.bedrock.base.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsTest {

    @Test
    public void mapToObj() {

        Map<String, Object> identityMap = new HashMap<>();
        identityMap.put("street", "123 Main St");
        identityMap.put("city", "New York");

        Map<String, String> releashipMap = new HashMap<>();
        releashipMap.put("city", "name");

        Identity identity = new Identity();
        Address address = new Address();
        address.setRemark("6");
        identity.setAddress(address);
        identity.setName("123");

        Map<String, Object> stringObjectMap = Maps.objToMap(identity);
        System.out.println(stringObjectMap);

        Identity identity1 = Maps.mapToObj(stringObjectMap, Identity.class);

        List<Object> collect = new ArrayList<>(identityMap.values());
        ArrayList<String> strings = new ArrayList<>(identityMap.keySet());

        System.out.println(identity1);
    }
}