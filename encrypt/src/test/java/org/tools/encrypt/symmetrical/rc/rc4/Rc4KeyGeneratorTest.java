package org.tools.encrypt.symmetrical.rc.rc4;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.Rc4Properties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.exception.symmetrical.rc.rc4.Rc4Exception;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Rc4KeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKey(int) / getSecretKeyStr /
 * getSecretKey(String,String) / getProperties
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class Rc4KeyGeneratorTest {
    /** 32 个 Hex 字符 = 16 字节 = 128 位 */
    private static final String HEX_KEY = "0102030405060708090a0b0c0d0e0f10";

    @Test
    public void testGetSecretKey() throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey();
        Assert.assertNotNull(key);
        // 默认 128 位 = 16 字节
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyWithKeyBits() throws Rc4Exception {
        Assert.assertEquals(8, Rc4KeyGenerator.getSecretKey(64).getEncoded().length);
        // 256 位在受限 JCE 策略下可能不可用，按当前允许的最大值取上限
        int maxAllowed;
        try {
            maxAllowed = Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC4.getAlgorithm());
        } catch (Exception e) {
            throw new Rc4Exception(e);
        }
        int keyBits = Math.min(256, maxAllowed);
        Assert.assertEquals((keyBits + 7) / 8, Rc4KeyGenerator.getSecretKey(keyBits).getEncoded().length);
    }

    @Test
    public void testGetSecretKeyMinimumKeyBits() throws Rc4Exception {
        // 最小 40 位，向上取整为 5 字节
        Assert.assertEquals(5, Rc4KeyGenerator.getSecretKey(40).getEncoded().length);
    }

    @Test(expected = Rc4Exception.class)
    public void testGetSecretKeyTooSmall() throws Rc4Exception {
        Rc4KeyGenerator.getSecretKey(39);
    }

    @Test(expected = Rc4Exception.class)
    public void testGetSecretKeyTooLarge() throws Rc4Exception {
        Rc4KeyGenerator.getSecretKey(1025);
    }

    /**
     * 超过有效上限（JCE 策略上限与 RC4 规范上限 1024 位中的较小值）时应拒绝。
     *
     * @throws Exception 算法查询失败时抛出
     */
    @Test
    public void testGetSecretKeyAboveMaxAllowed() throws Exception {
        int maxBits = Math.min(
                Cipher.getMaxAllowedKeyLength(AlgorithmEnum.RC4.getAlgorithm()), 1024);
        try {
            Rc4KeyGenerator.getSecretKey(maxBits + 1);
            Assert.fail("keyBits=" + (maxBits + 1) + " 应超过有效上限 " + maxBits + " 被拒绝");
        } catch (Rc4Exception e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetSecretKeyStr() throws Rc4Exception {
        String hex = Rc4KeyGenerator.getSecretKeyStr(Rc4KeyGenerator.getSecretKey());
        Assert.assertEquals(32, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws Rc4Exception {
        SecretKey key = Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), HEX_KEY);
        Assert.assertEquals(16, key.getEncoded().length);
        Assert.assertEquals(AlgorithmEnum.RC4.getAlgorithm(), key.getAlgorithm());
        Assert.assertEquals(HEX_KEY, Rc4KeyGenerator.getSecretKeyStr(key));
    }

    @Test(expected = Rc4Exception.class)
    public void testNonHexKeyRejected() throws Rc4Exception {
        Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), "这不是Hex密钥");
    }

    @Test(expected = Rc4Exception.class)
    public void testEmptyKeyRejected() throws Rc4Exception {
        Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), "");
    }

    /**
     * 解码后长度小于 5 字节时应被拒绝。
     *
     * @throws Rc4Exception 密钥解析失败时抛出
     */
    @Test(expected = Rc4Exception.class)
    public void testGetSecretKeyTooShortRejected() throws Rc4Exception {
        // 8 个 Hex 字符 = 4 字节，小于 5 字节下限
        Rc4KeyGenerator.getSecretKey(AlgorithmEnum.RC4.getAlgorithm(), "01234567");
    }

    @Test
    public void testGetProperties() throws Rc4Exception {
        Rc4Properties properties = Rc4KeyGenerator.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals(AlgorithmEnum.RC4.getAlgorithm(), properties.getAlgorithm());
        Assert.assertEquals(AlgorithmModeEnum.RC4.getAlgorithmMode(), properties.getAlgorithmMode());
        Assert.assertEquals(128, properties.getKeyBits());
    }
}
