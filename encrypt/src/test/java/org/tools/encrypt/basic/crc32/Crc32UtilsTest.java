package org.tools.encrypt.basic.crc32;

import org.junit.Assert;
import org.junit.Test;

/**
 * Crc32Utils 冒烟测试
 * 覆盖全部公开方法：encode
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class Crc32UtilsTest {
    @Test
    public void testEncode() {
        // CRC-32/ISO-HDLC 校验向量：CRC32("123456789") = 0xCBF43926
        Assert.assertEquals(3421780262L, Crc32Utils.encode("123456789".getBytes()));
        // 每次调用独立实例，连续调用结果一致（此前共享实例会累积）
        Assert.assertEquals(Crc32Utils.encode("abc".getBytes()), Crc32Utils.encode("abc".getBytes()));
    }
}
