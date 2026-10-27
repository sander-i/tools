package org.tools.encrypt.basic;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

/**
 * BasicUtils 冒烟测试
 * 覆盖全部公开方法：digest
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class BasicUtilsTest {
    @Test
    public void testDigest() throws NoSuchAlgorithmException {
        // 与 ShaUtils 的已知答案一致（同一 JCA 通道）
        Assert.assertArrayEquals(
                new byte[]{(byte) 0xba, 0x78, 0x16, (byte) 0xbf},
                java.util.Arrays.copyOf(BasicUtils.digest("SHA-256", "abc".getBytes()), 4));
        // MD5 摘要长度 16 字节
        Assert.assertEquals(16, BasicUtils.digest("MD5", "abc".getBytes()).length);
    }
}
