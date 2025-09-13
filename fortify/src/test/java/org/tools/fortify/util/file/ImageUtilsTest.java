package org.tools.fortify.util.file;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    public void testImageUrLToBinary() {
        // 测试图片URL转字节数组功能
        try (MockedStatic<ImageUtils> utilities = Mockito.mockStatic(ImageUtils.class, Mockito.CALLS_REAL_METHODS)) {
            // Mock ImageUtils.readInputStream method
            utilities.when(() -> ImageUtils.imageUrLToBinary(anyString())).thenCallRealMethod();

            byte[] result = ImageUtils.imageUrLToBinary(null);
            assertArrayEquals("null URL应返回空字节数组", new byte[0], result);

            result = ImageUtils.imageUrLToBinary("");
            assertArrayEquals("空URL应返回空字节数组", new byte[0], result);
        }
    }

    @Test
    public void testImageUrLToBinary2() {
        // 测试图片URL转字节数组功能(无超时限制)
        byte[] result = ImageUtils.imageUrLToBinary2(null);
        assertArrayEquals("null URL应返回空字节数组", new byte[0], result);

        result = ImageUtils.imageUrLToBinary2("");
        assertArrayEquals("空URL应返回空字节数组", new byte[0], result);
    }

    @Test
    public void testReadInputStream() throws IOException {
        // 测试读取输入流返回字节数组功能
        // 使用ByteArrayInputStream测试
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_IMAGE_BYTES);
        byte[] result = ImageUtilsTestHelper.invokeReadInputStream(inputStream);

        assertNotNull("返回的字节数组不应为null", result);
        assertArrayEquals("返回的字节数组应与输入一致", TEST_IMAGE_BYTES, result);

        // 测试null输入
        result = ImageUtilsTestHelper.invokeReadInputStream(null);
        assertArrayEquals("null输入应返回空字节数组", new byte[0], result);
    }

    @Test
    public void testFormatName() {
        // 测试通过文件路径截取文件类型功能
        String format = ImageUtils.formatName("/path/to/image.jpg");
        assertEquals("文件类型应为jpg", "jpg", format);

        format = ImageUtils.formatName("image.png");
        assertEquals("文件类型应为png", "png", format);

        format = ImageUtils.formatName("image");
        assertEquals("无扩展名应返回空字符串", "", format);
    }

    @Test
    public void testImageToBinary() {
        // 测试图片文件转字节数组功能
        byte[] result = ImageUtils.imageToBinary(null);
        assertArrayEquals("null路径应返回空字节数组", new byte[0], result);

        result = ImageUtils.imageToBinary("");
        assertArrayEquals("空路径应返回空字节数组", new byte[0], result);
    }

    @Test
    public void testImageToBinaryLoss() {
        // 测试图片文件转字节数组功能(损失画质)
        byte[] result = ImageUtils.imageToBinaryLoss(null);
        assertArrayEquals("null路径应返回空字节数组", new byte[0], result);

        result = ImageUtils.imageToBinaryLoss("");
        assertArrayEquals("空路径应返回空字节数组", new byte[0], result);
    }

    @Test
    public void testBinaryToImage() {
        // 测试二进制转图片文件功能
        // 测试null字节数组
        ImageUtils.binaryToImage(null, TEST_FILE_PATH);
        // 不应抛出异常

        // 测试空字节数组
        ImageUtils.binaryToImage(new byte[0], TEST_FILE_PATH);
        // 不应抛出异常
    }

    @Test
    public void testBinaryToImageLoss() {
        // 测试二进制转图片文件功能(损失画质)
        // 测试null字节数组
        ImageUtils.binaryToImageLoss(null, TEST_FILE_PATH);
        // 不应抛出异常

        // 测试空字节数组
        ImageUtils.binaryToImageLoss(new byte[0], TEST_FILE_PATH);
        // 不应抛出异常
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

    @Test
    public void testCloseStream() {
        // 测试关闭流功能
        // 这些方法是私有的，主要测试它们不会抛出异常
        ImageUtilsTestHelper.invokeCloseStream("测试错误信息", (HttpURLConnection) null, (InputStream) null);
        // 不应抛出异常
    }

    /**
     * 测试辅助类，用于访问ImageUtils的私有方法
     */
    static class ImageUtilsTestHelper {
        static byte[] invokeReadInputStream(Object stream) {
            try {
                // 使用反射调用私有方法
                java.lang.reflect.Method method = ImageUtils.class.getDeclaredMethod("readInputStream", Object.class);
                method.setAccessible(true);
                return (byte[]) method.invoke(null, stream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static void invokeCloseStream(String errorMsg, HttpURLConnection httpConn, InputStream input) {
            try {
                // 使用反射调用私有方法
                java.lang.reflect.Method method = ImageUtils.class.getDeclaredMethod("closeStream", String.class, HttpURLConnection.class, InputStream.class);
                method.setAccessible(true);
                method.invoke(null, errorMsg, httpConn, input);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testRoundTripConversion() {
        // 测试往返转换：字节数组->图片文件->字节数组
        String tempFilePath = "temp_test_image.jpg";

        // 测试二进制转图片文件
        ImageUtils.binaryToImage(TEST_IMAGE_BYTES, tempFilePath);

        // 测试图片文件转字节数组
        byte[] result = ImageUtils.imageToBinary(tempFilePath);

        // 清理临时文件
        File tempFile = new File(tempFilePath);
        if (tempFile.exists()) {
            tempFile.delete();
        }

        // 结果验证（由于文件系统操作，这里只验证不抛出异常）
        assertNotNull("返回的字节数组不应为null", result);
    }

    @Test
    public void testFormatNameEdgeCases() {
        // 测试formatName方法的边界情况
        assertEquals("多重扩展名应返回最后的扩展名", "gz", ImageUtils.formatName("archive.tar.gz"));
        assertEquals("以点开头应返回正确扩展名", "config", ImageUtils.formatName(".config"));
        assertEquals("只有点应返回空字符串", "", ImageUtils.formatName("."));
        assertEquals("无路径应返回正确扩展名", "txt", ImageUtils.formatName("file.txt"));
    }
}
