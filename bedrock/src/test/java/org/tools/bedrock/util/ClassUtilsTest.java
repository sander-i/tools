package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;
import org.tools.bedrock.base.Identity;
import org.tools.bedrock.base.Sex;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ClassUtilsTest {

    @Test
    public void objToMap() throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        Identity identity = new Identity();
        identity.setSex(Sex.MAN);
        Map<String, Object> stringObjectMap = ClassUtils.objToMap(identity);
        System.out.println(stringObjectMap);

        Map<String, String> relationship = new HashMap<>();
        relationship.put("sex","sex");

        Identity o = (Identity)ClassUtils.mapToObj(stringObjectMap, relationship, Identity.class);
        System.out.println(o);

    }
}