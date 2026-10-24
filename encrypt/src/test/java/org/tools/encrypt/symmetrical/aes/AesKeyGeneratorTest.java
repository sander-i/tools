package org.tools.encrypt.symmetrical.aes;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;

/**
 * AesKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKeyStr / getSecretKey(String,String) / getProperties
 *
 * @author sander 2026/10/24
 */
public class AesKeyGeneratorTest {

    @Test
    public void testGetSecretKey() throws AesException {
        SecretKey key = AesKeyGenerator.getSecretKey();
        Assert.assertEquals("AES", key.getAlgorithm());
        // 生成器固定 128 位
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyStr() throws AesException {
        String hex = AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey());
        Assert.assertEquals(32, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws AesException {
        String hex = AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey());
        SecretKey key = AesKeyGenerator.getSecretKey("AES", hex);
        Assert.assertArrayEquals(hex.getBytes(), AesKeyGenerator.getSecretKeyStr(key).getBytes());
    }

    @Test
    public void testGetProperties() throws AesException {
        AesProperties properties = AesKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
    }

}
