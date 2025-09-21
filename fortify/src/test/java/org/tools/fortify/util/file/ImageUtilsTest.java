package org.tools.fortify.util.file;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

/**
 * ImageUtils 冒烟测试
 * 测试基本的图片处理功能
 */
@RunWith(MockitoJUnitRunner.class)
public class ImageUtilsTest {

    private static final String TEST_FILE_PATH = "test.jpg";
    private static final String TEST_URL = "http://example.com/test.jpg";
    private static final byte[] TEST_IMAGE_BYTES = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

    @Before
    public void setUp() {
        // 初始化测试环境
    }

    @Test
    public void testCompressPicByScaleWithQuality() {
        // 测试按照质量压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 0.5f);
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithScale() {
        // 测试按照比例压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 0.5);
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithWidthHeight() {
        // 测试按照宽高压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 100, 100);
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithScaleAndQuality() {
        // 测试按照比例和质量压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 0.5, 0.8f);
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithWidthHeightAndQuality() {
        // 测试按照宽高和质量压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 100, 100, 0.8f);
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithWidthHeightQualityAndFormat() {
        // 测试按照宽高、质量、格式压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 100, 100, 0.8f, "png");
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testCompressPicByScaleWithScaleQualityAndFormat() {
        // 测试按照比例、质量、格式压缩图片功能
        byte[] result = ImageUtils.compressPicByScale(TEST_IMAGE_BYTES, 0.5, 0.8f, "png");
        assertNotNull("返回的字节数组不应为null", result);
    }

}
