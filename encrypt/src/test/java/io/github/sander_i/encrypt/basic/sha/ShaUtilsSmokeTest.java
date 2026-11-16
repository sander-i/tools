package io.github.sander_i.encrypt.basic.sha;

import junit.framework.TestCase;

import java.security.NoSuchAlgorithmException;

/**
 * SHA 系列冒烟测试：对照 NIST 标准测试向量
 *
 * @author sander
 */
public class ShaUtilsSmokeTest extends TestCase {

    /**
     * SHA-1("abc") 标准值
     */
    private static final String ABC_SHA1 = "a9993e364706816aba3e25717850c26c9cd0d89d";
    /**
     * SHA-256("abc") 标准值
     */
    private static final String ABC_SHA256 = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad";
    /**
     * SHA-384("abc") 标准值
     */
    private static final String ABC_SHA384 = "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed"
            + "8086072ba1e7cc2358baeca134c825a7";
    /**
     * SHA-512("abc") 标准值
     */
    private static final String ABC_SHA512 = "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a"
            + "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f";

    public void testSha1() throws NoSuchAlgorithmException {
        assertEquals(ABC_SHA1, ShaUtils.encodeSha1("abc"));
    }

    public void testSha256() throws NoSuchAlgorithmException {
        assertEquals(ABC_SHA256, ShaUtils.encodeSha256("abc"));
        // 默认算法即 SHA-256
        assertEquals(ABC_SHA256, ShaUtils.encode("abc"));
    }

    public void testSha384() throws NoSuchAlgorithmException {
        assertEquals(ABC_SHA384, ShaUtils.encodeSha384("abc"));
        assertEquals(96, ShaUtils.encodeSha384("abc").length());
    }

    public void testSha512() throws NoSuchAlgorithmException {
        assertEquals(ABC_SHA512, ShaUtils.encodeSha512("abc"));
        assertEquals(128, ShaUtils.encodeSha512("abc").length());
    }

    public void testEmptyInput() throws NoSuchAlgorithmException {
        // 空串的标准值
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", ShaUtils.encodeSha1(""));
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                ShaUtils.encodeSha256(""));
    }

    public void testChineseInput() throws NoSuchAlgorithmException {
        // 中文输入不应抛异常，且长度固定
        assertEquals(96, ShaUtils.encodeSha384("中文测试").length());
        assertEquals(128, ShaUtils.encodeSha512("中文测试").length());
    }
}
