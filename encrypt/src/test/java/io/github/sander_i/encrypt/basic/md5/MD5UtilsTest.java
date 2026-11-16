package io.github.sander_i.encrypt.basic.md5;

import org.junit.Test;
import io.github.sander_i.encrypt.exception.EncryptException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * MD5 算法测试
 *
 * @author sander
 */
public class MD5UtilsTest {

    /**
     * "abc" 的 MD5 标准值（大写）
     */
    private static final String ABC_UPPER = "900150983CD24FB0D6963F7D28E17F72";
    /**
     * "abc" 的 MD5 标准值（小写）
     */
    private static final String ABC_LOWER = "900150983cd24fb0d6963f7d28e17f72";

    @Test
    public void encode() throws EncryptException {
        assertEquals("默认输出大写十六进制", ABC_UPPER, MD5Utils.encode("abc"));
    }

    @Test
    public void testEncode() throws EncryptException {
        assertEquals("isLower=true 输出小写", ABC_LOWER, MD5Utils.encode("abc", true));
        assertEquals("isLower=false 输出大写", ABC_UPPER, MD5Utils.encode("abc", false));
    }

    @Test
    public void testEncode1() throws EncryptException {
        assertEquals("byte[] 重载与 String 重载结果一致", ABC_UPPER, MD5Utils.encode("abc".getBytes()));
        assertEquals(ABC_LOWER, MD5Utils.encode("abc".getBytes(), true));
    }

    @Test
    public void testEncode2() throws EncryptException {
        String h1 = MD5Utils.encode("666");
        assertNotNull(h1);
        assertEquals("MD5 输出固定 32 位", 32, h1.length());
        assertEquals("同输入同输出", h1, MD5Utils.encode("666"));
        assertNotEquals("不同输入不同输出", h1, MD5Utils.encode("667"));
    }

    @Test
    public void testProperties() throws EncryptException {
        MD5Properties properties = MD5Utils.getProperties();
        assertNotNull("未加载时也应返回默认配置", properties);
        properties.setAlgorithm("MD5");

        MD5Properties custom = new MD5Properties();
        custom.setAlgorithm("MD5");
        MD5Utils.load(custom);
        assertSame("load 后配置应被替换", custom, MD5Utils.getProperties());
    }

    @Test
    public void testChineseInput() throws EncryptException {
        String h = MD5Utils.encode("中文测试");
        assertNotNull(h);
        assertEquals(32, h.length());
        assertEquals("中文输入应稳定", h, MD5Utils.encode("中文测试"));
    }

    @Test
    public void testLoadRejectsNull() {
        try {
            MD5Utils.load(null);
            throw new AssertionError("expected EncryptException");
        } catch (EncryptException expected) {
            // 预期
        }
    }
}
