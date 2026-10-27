package org.tools.encrypt.symmetrical;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * SymmetricalUtils 冒烟测试
 * 覆盖全部公开方法：encode / decode（显式传参，不依赖全局配置）
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class SymmetricalUtilsTest {
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/CBC/PKCS5Padding";
    private static final SecretKey KEY =
            new SecretKeySpec(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, ALGORITHM);
    private static final String IV = "0123456789abcdef";
    private static final String PLAINTEXT = "SymmetricalUtils smoke 测试文本";

    @Test
    public void testEncodeDecodeRoundTrip() throws SymmetricalException {
        String ciphertext = SymmetricalUtils.encode(ALGORITHM, MODE, KEY, IV, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, SymmetricalUtils.decode(ALGORITHM, MODE, KEY, IV, ciphertext));
    }
}
