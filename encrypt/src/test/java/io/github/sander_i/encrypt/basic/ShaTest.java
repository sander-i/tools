package io.github.sander_i.encrypt.basic;

import org.junit.Test;
import io.github.sander_i.encrypt.basic.sha.ShaUtils;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * SHA 算法测试
 *
 * <p>
 * 对照 NIST 标准测试向量，确保摘要值正确。
 * </p>
 *
 * @author sander
 * @version 1.0
 */
public class ShaTest {

    @Test
    public void test() throws NoSuchAlgorithmException {
        String encode = ShaUtils.encode("SHA-512", "666");
        assertNotNull(encode);
        assertEquals("SHA-512 输出长度应为 128 个十六进制字符", 128, encode.length());
    }

    @Test
    public void testStandardVectors() throws NoSuchAlgorithmException {
        // NIST 标准测试向量："abc"
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", ShaUtils.encode("SHA-1", "abc"));
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                ShaUtils.encode("SHA-256", "abc"));
        assertEquals(96, ShaUtils.encode("SHA-384", "abc").length());
        assertEquals(128, ShaUtils.encode("SHA-512", "abc").length());
    }

    @Test
    public void testSameInputSameOutput() throws NoSuchAlgorithmException {
        assertEquals(ShaUtils.encode("SHA-256", "确定输入"), ShaUtils.encode("SHA-256", "确定输入"));
    }

    @Test
    public void testDifferentInputDifferentOutput() throws NoSuchAlgorithmException {
        assertNotEquals(ShaUtils.encode("SHA-256", "a"), ShaUtils.encode("SHA-256", "b"));
    }

    @Test
    public void testChineseInput() throws NoSuchAlgorithmException {
        String h = ShaUtils.encode("SHA-256", "中文测试");
        assertNotNull(h);
        assertEquals(64, h.length());
    }
}
