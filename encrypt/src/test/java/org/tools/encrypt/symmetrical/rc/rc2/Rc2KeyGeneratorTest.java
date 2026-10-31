package org.tools.encrypt.symmetrical.rc.rc2;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.Rc2Properties;
import org.tools.encrypt.exception.symmetrical.rc.rc2.Rc2Exception;

import javax.crypto.SecretKey;

/**
 * Rc2KeyGenerator 冒烟测试：覆盖全部公开方法（生成、Hex 解析、配置读取）。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc2KeyGeneratorTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 */
    private static final String HEX_KEY = "0123456789abcdef0123456789abcdef";

    @Test
    public void testGetSecretKey() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey();
        Assert.assertNotNull(key);
        Assert.assertEquals("RC2", key.getAlgorithm());
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyWithKeyBits() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey(64);
        Assert.assertEquals(8, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyMinimumKeyBits() throws Rc2Exception {
        // 最小 40 位，向上取整为 5 字节
        SecretKey key = Rc2KeyGenerator.getSecretKey(40);
        Assert.assertEquals(5, key.getEncoded().length);
    }

    @Test(expected = Rc2Exception.class)
    public void testGetSecretKeyTooSmall() throws Rc2Exception {
        Rc2KeyGenerator.getSecretKey(39);
    }

    @Test(expected = Rc2Exception.class)
    public void testGetSecretKeyTooLarge() throws Rc2Exception {
        Rc2KeyGenerator.getSecretKey(1025);
    }

    @Test
    public void testGetSecretKeyStr() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey();
        String hexKey = Rc2KeyGenerator.getSecretKeyStr(key);
        Assert.assertEquals(32, hexKey.length());
    }

    @Test
    public void testGetSecretKeyFromHex() throws Rc2Exception {
        SecretKey key = Rc2KeyGenerator.getSecretKey("RC2", HEX_KEY);
        Assert.assertEquals(16, key.getEncoded().length);
        Assert.assertEquals(HEX_KEY, Rc2KeyGenerator.getSecretKeyStr(key));
    }

    @Test(expected = Rc2Exception.class)
    public void testGetSecretKeyNonHexRejected() throws Rc2Exception {
        Rc2KeyGenerator.getSecretKey("RC2", "这不是Hex密钥");
    }

    /**
     * 解码后长度小于 5 字节时应被拒绝。
     *
     * @throws Rc2Exception 密钥解析失败时抛出
     */
    @Test(expected = Rc2Exception.class)
    public void testGetSecretKeyTooShortRejected() throws Rc2Exception {
        // 8 个 Hex 字符 = 4 字节，小于 5 字节下限
        Rc2KeyGenerator.getSecretKey("RC2", "01234567");
    }

    @Test
    public void testGetProperties() throws Rc2Exception {
        Rc2Properties properties = Rc2KeyGenerator.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals("RC2", properties.getAlgorithm());
        Assert.assertEquals("RC2", properties.getAlgorithmMode());
        Assert.assertEquals(128, properties.getKeyBits());
    }
}
