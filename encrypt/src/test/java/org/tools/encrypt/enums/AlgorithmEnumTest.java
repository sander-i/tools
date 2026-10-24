package org.tools.encrypt.enums;

import org.junit.Assert;
import org.junit.Test;

/**
 * 算法枚举冒烟测试
 * 覆盖全部公开方法：equals / enumName / getAlgorithm / getAlgorithmMode / getSignAlgorithm
 *
 * @author sander 2026/10/24
 */
public class AlgorithmEnumTest {

    @Test
    public void testAlgorithmEnum() {
        Assert.assertEquals("AES", AlgorithmEnum.AES.getAlgorithm());
        Assert.assertTrue(AlgorithmEnum.AES.equals("AES"));
        Assert.assertFalse(AlgorithmEnum.AES.equals("DES"));
        Assert.assertEquals(AlgorithmEnum.RSA, AlgorithmEnum.enumName("RSA"));
    }

    @Test
    public void testAlgorithmModeEnum() {
        Assert.assertEquals("AES/CBC/PKCS5Padding", AlgorithmModeEnum.AES_CBC_PKCS5PADDING.getAlgorithmMode());
        Assert.assertTrue(AlgorithmModeEnum.AES_CBC_PKCS5PADDING.equals("AES/CBC/PKCS5Padding"));
        Assert.assertEquals(AlgorithmModeEnum.DES, AlgorithmModeEnum.enumName("DES"));
    }

    @Test
    public void testSignAlgorithmEnum() {
        Assert.assertEquals("SHA256withRSA", SignAlgorithmEnum.SHA256_WITH_RSA.getSignAlgorithm());
        Assert.assertTrue(SignAlgorithmEnum.SHA256_WITH_RSA.equals("SHA256withRSA"));
        Assert.assertEquals(SignAlgorithmEnum.SHA256_WITH_DSA, SignAlgorithmEnum.enumName("SHA256withDSA"));
    }

}
