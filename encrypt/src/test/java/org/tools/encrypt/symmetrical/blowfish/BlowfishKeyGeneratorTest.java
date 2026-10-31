package org.tools.encrypt.symmetrical.blowfish;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.entity.symmetrical.BlowfishProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.symmetrical.blowfish.BlowfishException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * BlowfishKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getSecretKey / getSecretKey(int) / getSecretKeyStr /
 * getSecretKey(String,String) / getProperties
 *
 * @author sander
 * @since 2.0.1
 */
public class BlowfishKeyGeneratorTest {
    @Test
    public void testGetSecretKey() throws BlowfishException {
        SecretKey key = BlowfishKeyGenerator.getSecretKey();
        Assert.assertEquals(AlgorithmEnum.BLOWFISH.getAlgorithm(), key.getAlgorithm());
        // 默认 128 位 = 16 字节
        Assert.assertEquals(16, key.getEncoded().length);
    }

    @Test
    public void testGetSecretKeyWithKeyBits() throws Exception {
        Assert.assertEquals(8, BlowfishKeyGenerator.getSecretKey(64).getEncoded().length);
        // 256 位在受限 JCE 策略下可能不可用，按当前允许的最大值取上限
        int maxAllowed = Cipher.getMaxAllowedKeyLength(AlgorithmEnum.BLOWFISH.getAlgorithm());
        int keyBits = Math.min(256, maxAllowed);
        Assert.assertEquals(keyBits / 8, BlowfishKeyGenerator.getSecretKey(keyBits).getEncoded().length);
    }

    @Test
    public void testGetSecretKeyStr() throws BlowfishException {
        String hex = BlowfishKeyGenerator.getSecretKeyStr(BlowfishKeyGenerator.getSecretKey());
        Assert.assertEquals(32, hex.length());
        Assert.assertTrue(hex.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testGetSecretKeyFromString() throws BlowfishException {
        String hex = BlowfishKeyGenerator.getSecretKeyStr(BlowfishKeyGenerator.getSecretKey());
        SecretKey key = BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), hex);
        Assert.assertEquals(16, key.getEncoded().length);
        Assert.assertEquals(hex, BlowfishKeyGenerator.getSecretKeyStr(key));
    }

    @Test
    public void testGetProperties() throws BlowfishException {
        BlowfishProperties properties = BlowfishKeyGenerator.getProperties();
        Assert.assertNotNull(properties.getSecretKey());
        Assert.assertNotNull(properties.getSecretKeyStr());
        Assert.assertEquals(AlgorithmEnum.BLOWFISH.getAlgorithm(), properties.getAlgorithm());
    }

    @Test
    public void testKeyBitsNotMultipleOf8Rejected() {
        try {
            BlowfishKeyGenerator.getSecretKey(100);
            Assert.fail("keyBits=100 不是 8 的整数倍，应被拒绝");
        } catch (BlowfishException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testKeyBitsTooSmallRejected() {
        try {
            BlowfishKeyGenerator.getSecretKey(16);
            Assert.fail("keyBits=16 应被拒绝");
        } catch (BlowfishException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testKeyBitsTooLargeRejected() {
        try {
            BlowfishKeyGenerator.getSecretKey(512);
            Assert.fail("keyBits=512 应被拒绝");
        } catch (BlowfishException e) {
            Assert.assertNotNull(e.getMessage());
        }
    }

    @Test(expected = BlowfishException.class)
    public void testNonHexKeyRejected() throws BlowfishException {
        BlowfishKeyGenerator.getSecretKey(AlgorithmEnum.BLOWFISH.getAlgorithm(), "这不是Hex密钥");
    }
}
