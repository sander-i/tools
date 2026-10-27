package org.tools.encrypt.symmetrical.des;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.exception.symmetrical.des.DesException;

import javax.crypto.SecretKey;

/**
 * DesKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKeyStr / getSecretKey(String,String) / getProperties
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class DesKeyGeneratorTest {
    @Test
    public void testGetSecretKey() throws DesException {
        SecretKey key = DesKeyGenerator.getSecretKey();
        Assert.assertEquals("DES", key.getAlgorithm());
        Assert.assertEquals(8, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyStr() throws DesException {
        String hex = DesKeyGenerator.getSecretKeyStr(DesKeyGenerator.getSecretKey());
        Assert.assertEquals(16, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-fA-F]{16}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws DesException {
        // DES 密钥为原始字符串，UTF-8 编码后必须恰好 8 字节
        SecretKey key = DesKeyGenerator.getSecretKey("DES", "BeYour18");
        Assert.assertEquals(8, key.getEncoded().length);
    }

    @Test
    public void testGetProperties() throws DesException {
        DesProperties properties = DesKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
    }
}
