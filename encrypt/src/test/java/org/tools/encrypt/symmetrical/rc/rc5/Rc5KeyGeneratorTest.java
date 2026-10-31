package org.tools.encrypt.symmetrical.rc.rc5;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.Rc5Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc5.Rc5Exception;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Rc5KeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKey(int) / getSecretKeyStr /
 * getSecretKey(String,String) / getProperties，以及密钥位数越界校验。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc5KeyGeneratorTest {
    @Test
    public void testGetSecretKey() throws Rc5Exception {
        SecretKey key = Rc5KeyGenerator.getSecretKey();
        Assert.assertEquals(AlgorithmEnum.RC5.getAlgorithm(), key.getAlgorithm());
        // 默认 128 位 = 16 字节
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyWithKeyBits() throws Rc5Exception {
        Assert.assertEquals(8, Rc5KeyGenerator.getSecretKey(64).getEncoded().length);
    }

    @Test
    public void testGetSecretKeyMinimumKeyBits() throws Rc5Exception {
        Assert.assertEquals(1, Rc5KeyGenerator.getSecretKey(8).getEncoded().length);
    }

    @Test
    public void testGetSecretKeyAtMaxAllowedKeyLength() throws Exception {
        // 先触发 BC 注册，使 getMaxAllowedKeyLength 能解析 RC5
        Rc5KeyGenerator.getSecretKey();
        // JDK 9+ 默认无限策略返回 Integer.MAX_VALUE，实现按 RFC 2040 的 255 字节上限收敛
        int maxBits = Math.min(Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC5.getAlgorithm()), 255 * 8);
        Assert.assertEquals(maxBits / 8, Rc5KeyGenerator.getSecretKey(maxBits).getEncoded().length);
    }

    @Test(expected = Rc5Exception.class)
    public void testGetSecretKeyTooSmall() throws Rc5Exception {
        Rc5KeyGenerator.getSecretKey(7);
    }

    @Test
    public void testGetSecretKeyTooLarge() throws Exception {
        // 先触发 BC 注册，使 getMaxAllowedKeyLength 能解析 RC5
        Rc5KeyGenerator.getSecretKey();
        int maxBits = Math.min(Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC5.getAlgorithm()), 255 * 8);
        try {
            Rc5KeyGenerator.getSecretKey(maxBits + 1);
            Assert.fail("keyBits 超过允许的最大值时应被拒绝");
        } catch (Rc5Exception e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSecretKeyStr() throws Rc5Exception {
        String hex = Rc5KeyGenerator.getSecretKeyStr(Rc5KeyGenerator.getSecretKey());
        Assert.assertEquals(32, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws Rc5Exception {
        String hex = Rc5KeyGenerator.getSecretKeyStr(Rc5KeyGenerator.getSecretKey());
        SecretKey key = Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), hex);
        Assert.assertEquals(16, key.getEncoded().length);
        Assert.assertEquals(hex, Rc5KeyGenerator.getSecretKeyStr(key));
    }

    @Test(expected = Rc5Exception.class)
    public void testGetSecretKeyNonHexRejected() throws Rc5Exception {
        Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), "这不是Hex密钥");
    }

    @Test(expected = Rc5Exception.class)
    public void testGetSecretKeyEmptyRejected() throws Rc5Exception {
        Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), "");
    }

    /**
     * 解码后长度超过 RFC 2040 的 255 字节上限时应被拒绝。
     *
     * @throws Rc5Exception 密钥解析失败时抛出
     */
    @Test(expected = Rc5Exception.class)
    public void testGetSecretKeyTooLongRejected() throws Rc5Exception {
        // 512 个 Hex 字符 = 256 字节，超过 255 字节上限
        char[] chars = new char[512];
        java.util.Arrays.fill(chars, 'a');
        Rc5KeyGenerator.getSecretKey(AlgorithmEnum.RC5.getAlgorithm(), new String(chars));
    }

    @Test
    public void testGetProperties() throws Rc5Exception {
        Rc5Properties properties = Rc5KeyGenerator.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals(AlgorithmEnum.RC5.getAlgorithm(), properties.getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.RC5.getAlgorithmMode(), properties.getAlgorithmMode());
        Assert.assertEquals(128, properties.getKeyBits());
    }
}
