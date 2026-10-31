package org.tools.encrypt.symmetrical.des.des3;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.Des3Properties;
import org.tools.encrypt.exception.symmetrical.des3.Des3Exception;

import javax.crypto.SecretKey;

/**
 * Des3KeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKey(int) / getSecretKeyStr /
 * getSecretKey(String,String) / getProperties
 *
 * @author sander
 * @since 2.0.1
 */
public class Des3KeyGeneratorTest {
    /** 32 个 Hex 字符 = 16 字节 = 两密钥 3DES */
    private static final String HEX_KEY_16 = "0123456789abcdeffedcba9876543210";

    /** 48 个 Hex 字符 = 24 字节 = 三密钥 3DES */
    private static final String HEX_KEY_24 = "0123456789abcdeffedcba987654321089abcdef01234567";

    @Test
    public void testGetSecretKey() throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey();
        Assert.assertEquals("DESede", key.getAlgorithm());
        Assert.assertEquals(24, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyWithKeyBits() throws Des3Exception {
        // JCA 的 DESede KeyGenerator 只接受 112 / 168 位，生成的密钥对象均为 24 字节
        Assert.assertEquals(24, Des3KeyGenerator.getSecretKey(112).getEncoded().length);
        Assert.assertEquals(24, Des3KeyGenerator.getSecretKey(168).getEncoded().length);
    }

    @Test(expected = Des3Exception.class)
    public void testGetSecretKeyWithUnsupportedKeyBits() throws Des3Exception {
        Des3KeyGenerator.getSecretKey(128);
    }

    @Test
    public void testGetSecretKeyStr() throws Des3Exception {
        String hex = Des3KeyGenerator.getSecretKeyStr(Des3KeyGenerator.getSecretKey());
        Assert.assertEquals(48, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{48}"));
    }

    @Test
    public void testGetSecretKeyFromHex16Bytes() throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey("DESede", HEX_KEY_16);
        Assert.assertEquals("DESede", key.getAlgorithm());
        // 两密钥 3DES（16 字节）在 JCA 中扩展为 K1‖K2‖K1 的 24 字节形式
        byte[] encoded = key.getEncoded();
        Assert.assertEquals(24, encoded.length);
        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(encoded[i], encoded[16 + i]);
        }
    }

    @Test
    public void testGetSecretKeyFromHex24Bytes() throws Des3Exception {
        SecretKey key = Des3KeyGenerator.getSecretKey("DESede", HEX_KEY_24);
        Assert.assertEquals("DESede", key.getAlgorithm());
        Assert.assertEquals(24, key.getEncoded().length);
    }

    @Test(expected = Des3Exception.class)
    public void testGetSecretKeyFromHexWithInvalidLength() throws Des3Exception {
        // 40 个 Hex 字符 = 20 字节，既不是 16 也不是 24 字节
        Des3KeyGenerator.getSecretKey("DESede", "0123456789abcdef0123456789abcdef01234567");
    }

    @Test(expected = Des3Exception.class)
    public void testGetSecretKeyFromNonHex() throws Des3Exception {
        Des3KeyGenerator.getSecretKey("DESede", "这不是Hex密钥");
    }

    @Test(expected = Des3Exception.class)
    public void testGetSecretKeyFromEmpty() throws Des3Exception {
        Des3KeyGenerator.getSecretKey("DESede", "");
    }

    @Test
    public void testGetProperties() throws Des3Exception {
        Des3Properties properties = Des3KeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals("DESede", properties.getAlgorithm());
        Assert.assertEquals("DESede/CBC/PKCS5Padding", properties.getAlgorithmMode());
        Assert.assertEquals(168, properties.getKeyBits());
        Assert.assertEquals(48, properties.getSecretKeyStr().length());
    }
}
