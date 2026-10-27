package org.tools.encrypt.basic.sha;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * ShaUtils 冒烟测试
 * 覆盖全部公开方法：encode×2
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class ShaUtilsTest {
    /** FIPS 180-4 经典向量：SHA-256("abc") */
    private static final String SHA256_ABC = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";

    @Test
    public void testEncode() throws NoSuchAlgorithmException {
        Assert.assertEquals(SHA256_ABC, ShaUtils.encode("abc"));
        Assert.assertEquals(64, ShaUtils.encode("666").length());
    }

    @Test
    public void testEncodeWithAlgorithm() throws NoSuchAlgorithmException {
        // SHA-512 为 128 个十六进制字符
        Assert.assertEquals(128, ShaUtils.encode("SHA-512", "666").length());
        // 同一原文不同算法摘要不同
        Assert.assertNotEquals(ShaUtils.encode("SHA-256", "666"), ShaUtils.encode("SHA-1", "666"));
    }
}
