package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * ThumbnailUtil 冒烟测试类
 * 用于验证 ThumbnailUtil 类的基本功能是否正常工作
 */
public class ThumbnailUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    /**
     * 从资源文件加载测试图片
     */
    private BufferedImage loadTestImage() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);
        return ImageIO.read(resourceURL);
    }

    /**
     * 将BufferedImage转换为字节数组用于测试
     */
    private byte[] imageToBytes(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    @Test
    public void testCreateThumbnailWithDimensions() throws IOException {
        BufferedImage testImage = loadTestImage();

        // 测试基于尺寸创建缩略图
        BufferedImage thumbnail = ThumbnailUtil.createThumbnail(testImage, 50, 50);

        assertNotNull("缩略图不应为null", thumbnail);
        assertEquals("缩略图宽度应为50", 50, thumbnail.getWidth());
        assertEquals("缩略图高度应为50", 50, thumbnail.getHeight());
    }

    @Test
    public void testCreateThumbnailWithScale() throws IOException {
        BufferedImage testImage = loadTestImage();

        // 测试基于比例创建缩略图
        BufferedImage thumbnail = ThumbnailUtil.createThumbnail(testImage, 0.5);

        assertNotNull("缩略图不应为null", thumbnail);
        // 由于原图是1200x848，缩放0.5后应该是600x424
        assertEquals("缩略图宽度应为50", 600, thumbnail.getWidth());
        assertEquals("缩略图高度应为50", 424, thumbnail.getHeight());
    }

    @Test
    public void testCreateThumbnailFromBytes() throws IOException {
        BufferedImage testImage = loadTestImage();
        byte[] imageBytes = imageToBytes(testImage, "jpg");

        // 测试基于字节数组创建缩略图
        BufferedImage thumbnail = ThumbnailUtil.createThumbnail(imageBytes, 50, 50, 0.8f);

        assertNotNull("缩略图不应为null", thumbnail);
        assertEquals("缩略图宽度应为50", 50, thumbnail.getWidth());
        assertEquals("缩略图高度应为50", 50, thumbnail.getHeight());
    }

    @Test
    public void testCreateThumbnailFromBytesWithScale() throws IOException {
        BufferedImage testImage = loadTestImage();
        byte[] imageBytes = imageToBytes(testImage, "jpg");

        // 测试基于字节数组和比例创建缩略图
        BufferedImage thumbnail = ThumbnailUtil.createThumbnail(imageBytes, 0.5, 0.8f);

        assertNotNull("缩略图不应为null", thumbnail);
        // 原图尺寸未知，但缩略图应该成功创建
        assertTrue("缩略图宽度应大于0", thumbnail.getWidth() > 0);
        assertTrue("缩略图高度应大于0", thumbnail.getHeight() > 0);
    }

    @Test
    public void testCreateThumbnailToFile() throws IOException {
        BufferedImage testImage = loadTestImage();

        // 创建缩略图
        BufferedImage thumbnail = ThumbnailUtil.createThumbnail(testImage, 50, 50);

        // 保存到临时文件
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, "thumbnail_test.jpg");

        try {
            // 写入文件
            ImageIO.write(thumbnail, "jpg", tempFile);

            // 验证文件存在且大小大于0
            assertTrue("临时文件应存在", tempFile.exists());
            assertTrue("临时文件大小应大于0", tempFile.length() > 0);

            // 读取并验证图像尺寸
            BufferedImage savedImage = ImageIO.read(tempFile);
            assertNotNull("应能从文件读取图像", savedImage);
            assertEquals("保存的图像宽度应为50", 50, savedImage.getWidth());
            assertEquals("保存的图像高度应为50", 50, savedImage.getHeight());
        } finally {
            // 清理临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testCreateThumbnailWithInvalidArguments() throws IOException {
        BufferedImage testImage = loadTestImage();

        // 测试无效参数
        try {
            ThumbnailUtil.createThumbnail(testImage, 0, 50);
            fail("宽度为0时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, 50, 0);
            fail("高度为0时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, -10, 50);
            fail("宽度为负数时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, 50, -10);
            fail("高度为负数时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail((BufferedImage) null, 50, 50);
            fail("原图为空时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, 0);
            fail("缩放比例为0时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, 1.5);
            fail("缩放比例大于1时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(testImage, -0.5);
            fail("缩放比例为负数时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }
    }

    @Test
    public void testCreateThumbnailFromBytesWithInvalidArguments() throws IOException {
        BufferedImage testImage = loadTestImage();
        byte[] imageBytes;
        try {
            imageBytes = imageToBytes(testImage, "jpg");
        } catch (IOException e) {
            fail("创建测试图片失败: " + e.getMessage());
            return;
        }

        // 测试无效参数
        try {
            ThumbnailUtil.createThumbnail((byte[]) null, 50, 50, 0.8f);
            fail("传入null字节数组应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(new byte[0], 50, 50, 0.8f);
            fail("传入空字节数组应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(imageBytes, 0, 50, 0.8f);
            fail("宽度为0时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(imageBytes, 50, 0, 0.8f);
            fail("高度为0时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(imageBytes, -10, 50, 0.8f);
            fail("宽度为负数时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }

        try {
            ThumbnailUtil.createThumbnail(imageBytes, 50, -10, 0.8f);
            fail("高度为负数时应抛出IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // 预期异常
        }
    }
}