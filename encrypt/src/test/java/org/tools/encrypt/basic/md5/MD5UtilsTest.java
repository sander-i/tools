package org.tools.encrypt.basic.md5;

import org.junit.Test;
import org.tools.encrypt.exception.EncryptException;

public class MD5UtilsTest {

    @Test
    public void encode() throws EncryptException {
        String encode = MD5Utils.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Utils.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD5");
        MD5Utils.setProperties(properties);
        encode = MD5Utils.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode() throws EncryptException {
        String encode = MD5Utils.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Utils.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Utils.setProperties(properties);
        encode = MD5Utils.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode1() throws EncryptException {
        String encode = MD5Utils.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Utils.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Utils.setProperties(properties);
        encode = MD5Utils.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode2() throws EncryptException {
        String encode = MD5Utils.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Utils.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Utils.setProperties(properties);
        encode = MD5Utils.encode("666");
        System.out.println(encode);
    }
}