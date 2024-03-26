package org.tools.encrypt.basic.md5;

import org.junit.Test;
import org.tools.encrypt.exception.EncryptException;

import static org.junit.Assert.*;

public class MD5ToolTest {

    @Test
    public void encode() throws EncryptException {
        String encode = MD5Tool.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Tool.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Tool.setProperties(properties);
        encode = MD5Tool.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode() throws EncryptException {
        String encode = MD5Tool.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Tool.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Tool.setProperties(properties);
        encode = MD5Tool.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode1() throws EncryptException {
        String encode = MD5Tool.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Tool.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Tool.setProperties(properties);
        encode = MD5Tool.encode("666");
        System.out.println(encode);
    }

    @Test
    public void testEncode2() throws EncryptException {
        String encode = MD5Tool.encode("666");
        System.out.println(encode);
        MD5Properties properties = MD5Tool.getProperties();
        System.out.println(properties);
        properties.setAlgorithm("MD6");
        MD5Tool.setProperties(properties);
        encode = MD5Tool.encode("666");
        System.out.println(encode);
    }
}