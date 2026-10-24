package org.tools.encrypt.symmetrical.idea;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import javax.crypto.SecretKey;

/**
 * IdeaKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKeyStr / getSecretKey(String,String) / getProperties
 *
 * @author sander 2026/10/24
 */
public class IdeaKeyGeneratorTest {

    @Test
    public void testGetSecretKey() throws IdeaException {
        SecretKey key = IdeaKeyGenerator.getSecretKey();
        Assert.assertEquals("IDEA", key.getAlgorithm());
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyStr() throws IdeaException {
        String hex = IdeaKeyGenerator.getSecretKeyStr(IdeaKeyGenerator.getSecretKey());
        Assert.assertEquals(32, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws IdeaException {
        // String 重载为 Hex 语义（与 getSecretKeyStr 对称），32 个 Hex 字符 = 128 位
        String hex = IdeaKeyGenerator.getSecretKeyStr(IdeaKeyGenerator.getSecretKey());
        SecretKey key = IdeaKeyGenerator.getSecretKey("IDEA", hex);
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetProperties() throws IdeaException {
        IdeaProperties properties = IdeaKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
    }

}
