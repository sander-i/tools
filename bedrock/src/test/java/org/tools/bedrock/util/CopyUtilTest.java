package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;
import org.tools.bedrock.base.Identity;
import org.tools.bedrock.base.Sex;

public class CopyUtilTest {

    @Test
    public void copyObj() {

        Identity identity = new Identity();
        identity.setId(5L);
        identity.setSex(Sex.MAN);
        identity.setName("张三");
        identity.setIdNo("123456789012345678");

        Address address = new Address();
        address.setRemark("煤油");
        identity.setAddress(address);

        Identity identity1 = CopyUtil.copyObj(identity, Identity.class, "name", "remark");
        System.out.println(identity1);
    }
}