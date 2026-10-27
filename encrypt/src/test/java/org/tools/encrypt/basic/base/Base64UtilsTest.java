package org.tools.encrypt.basic.base;

import org.junit.Assert;
import org.junit.Test;

/**
 * Base64Utils 冒烟测试
 * 覆盖全部公开方法：encode×2 / decode
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class Base64UtilsTest {
    @Test
    public void testEncodeBytes() {
        Assert.assertEquals("SGVsbG8gV29ybGQ=", Base64Utils.encode("Hello World".getBytes()));
    }

    @Test
    public void testEncodeString() {
        Assert.assertEquals("SGVsbG8gV29ybGQ=", Base64Utils.encode("Hello World"));
    }

    @Test
    public void testDecode() {
        Assert.assertEquals("Hello World", Base64Utils.decode("SGVsbG8gV29ybGQ="));
        // round-trip（含中文，UTF-8）
        String raw = "Base64 冒烟测试";
        Assert.assertEquals(raw, Base64Utils.decode(Base64Utils.encode(raw)));
    }
}
