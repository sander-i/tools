package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;
import org.tools.bedrock.base.Identity;
import org.tools.bedrock.base.Sex;

import static org.junit.Assert.*;

public class CopyUtilTest {

    @Test
    public void copyObj() {

        Identity identity = new Identity();
        identity.setId(5L);
        identity.setSex(Sex.MAN);

//        Address address = new Address();
//        address.setRemark("煤油");
//        identity.setAddress(address);

        Identity identity1 = CopyUtil.copyObj(identity, Identity.class);
        System.out.println(identity1);

    }
}