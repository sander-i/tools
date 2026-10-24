package org.tools.encrypt;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;

/**
 * EncryptKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getKeyGenerator / getKeyPairGenerator
 *
 * @author sander 2026/10/24
 */
public class EncryptKeyGeneratorTest {

    @Test
    public void testGetKeyGenerator() throws Exception {
        KeyGenerator generator = EncryptKeyGenerator.getKeyGenerator("AES");
        Assert.assertEquals("AES", generator.getAlgorithm());
        // 同算法复用同一实例
        Assert.assertSame(generator, EncryptKeyGenerator.getKeyGenerator("AES"));
        // 切换算法会重建
        Assert.assertEquals("DES", EncryptKeyGenerator.getKeyGenerator("DES").getAlgorithm());
    }

    @Test
    public void testGetKeyPairGenerator() throws Exception {
        KeyPairGenerator generator = EncryptKeyGenerator.getKeyPairGenerator("RSA");
        Assert.assertEquals("RSA", generator.getAlgorithm());
        Assert.assertSame(generator, EncryptKeyGenerator.getKeyPairGenerator("RSA"));
        Assert.assertEquals("DSA", EncryptKeyGenerator.getKeyPairGenerator("DSA").getAlgorithm());
    }

}
