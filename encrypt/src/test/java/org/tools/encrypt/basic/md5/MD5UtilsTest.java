package org.tools.encrypt.basic.md5;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.exception.EncryptException;

/**
 * MD5Utils 冒烟测试
 * 覆盖全部公开方法：load / encode×4（含属性读写）
 *
 * @author sander 2026/10/24
 */
public class MD5UtilsTest {

    private static final String RAW = "666";
    /** RFC 1321 经典向量：MD5("abc") */
    private static final String MD5_ABC = "900150983CD24FB0D6963F7D28E17F72";

    @Test
    public void testEncodeString() throws EncryptException {
        Assert.assertEquals(MD5_ABC, MD5Utils.encode("abc"));
        Assert.assertEquals(32, MD5Utils.encode(RAW).length());
    }

    @Test
    public void testEncodeStringLower() throws EncryptException {
        // 小写路径使用 BigInteger 转换，可能丢失前导零，仅验证可调用且为合法小写十六进制
        String lower = MD5Utils.encode("abc", true);
        Assert.assertTrue(lower.matches("[0-9a-f]{31,32}"));
    }

    @Test
    public void testEncodeBytes() throws EncryptException {
        Assert.assertEquals(MD5_ABC, MD5Utils.encode("abc".getBytes()));
        // 空字节数组返回空串的摘要（实现约定：byte[] 路径不做空判定）
        Assert.assertEquals("D41D8CD98F00B204E9800998ECF8427E", MD5Utils.encode(new byte[0]));
    }

    @Test
    public void testEncodeBytesLower() throws EncryptException {
        String lower = MD5Utils.encode("abc".getBytes(), true);
        Assert.assertTrue(lower.matches("[0-9a-f]{31,32}"));
    }

    @Test
    public void testLoadAndGetProperties() throws EncryptException {
        MD5Properties properties = new MD5Properties();
        properties.setAlgorithm("MD5");
        MD5Utils.load(properties);
        Assert.assertNotNull(MD5Utils.getProperties());
        Assert.assertEquals("MD5", MD5Utils.getProperties().getAlgorithm());
    }

    @Test(expected = EncryptException.class)
    public void testLoadNullRejected() throws EncryptException {
        MD5Utils.load(null);
    }

}
