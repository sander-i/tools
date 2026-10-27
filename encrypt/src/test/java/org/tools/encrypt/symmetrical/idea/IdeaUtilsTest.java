package org.tools.encrypt.symmetrical.idea;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

/**
 * IdeaUtils 冒烟测试
 * 覆盖全部公开方法：encode / decode（显式传参，不依赖全局配置）
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class IdeaUtilsTest {
    private static final String ALGORITHM = "IDEA";
    private static final String MODE = "IDEA/ECB/ISO10126Padding";
    private static final SecretKey KEY =
            new SecretKeySpec("0123456789abcdef".getBytes(StandardCharsets.UTF_8), ALGORITHM);
    private static final String PLAINTEXT = "IDEA utils smoke 测试文本";

    @Test
    public void testEncodeDecodeRoundTrip() throws IdeaException {
        String ciphertext = IdeaUtils.encode(ALGORITHM, MODE, KEY, PLAINTEXT);
        Assert.assertNotEquals(PLAINTEXT, ciphertext);
        Assert.assertEquals(PLAINTEXT, IdeaUtils.decode(ALGORITHM, MODE, KEY, ciphertext));
    }

    /**
     * Base64 已对齐为 JDK 严格实现（此前 commons-codec 宽容解析，非法字符不报错）：
     * 非法密文抛 IllegalArgumentException——与 SymmetricalUtils/Base64Utils 行为一致。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalidBase64Rejected() {
        IdeaUtils.decode(ALGORITHM, MODE, KEY, "!!非法Base64!!");
    }
}
