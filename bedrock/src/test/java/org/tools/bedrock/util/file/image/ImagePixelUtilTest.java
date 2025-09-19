package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * ImagePixelUtil 冒烟测试类
 * 测试图像像素处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImagePixelUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    /**
     * 创建一个简单的测试图片
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @param color  图片颜色
     * @return 测试图片
     */
    private BufferedImage createTestImage(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    @Test
    public void testConvertToBinary() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        BufferedImage binaryImage = ImagePixelUtil.convertToBinary(image, 128);

        assertNotNull("Binary image should not be null", binaryImage);
        assertEquals("Binary image width should match original", image.getWidth(), binaryImage.getWidth());
        assertEquals("Binary image height should match original", image.getHeight(), binaryImage.getHeight());
    }

    @Test
    public void testGetGrayValue() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        int grayValue = ImagePixelUtil.getGrayValue(image, 10, 10);

        assertTrue("Gray value should be between 0 and 255", grayValue >= 0 && grayValue <= 255);
    }

    @Test
    public void testConvertToGray() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        BufferedImage grayImage = ImagePixelUtil.convertToGray(image);

        assertNotNull("Gray image should not be null", grayImage);
        assertEquals("Gray image width should match original", image.getWidth(), grayImage.getWidth());
        assertEquals("Gray image height should match original", image.getHeight(), grayImage.getHeight());
    }

    @Test
    public void testCalculatePixelSimilarity() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        double similarity = ImagePixelUtil.calculatePixelSimilarity(image, image);

        assertEquals("Similarity of same images should be 1.0", 1.0, similarity, 0.001);
    }

    @Test
    public void testCalculatePixelSimilarityWithResize() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImagePixelUtil.calculatePixelSimilarityWithResize(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testImagePixelUtilSmoke() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);

        // 测试二值化
        BufferedImage binaryImage = ImagePixelUtil.convertToBinary(image, 128);
        assertNotNull("Binary image should not be null", binaryImage);

        // 测试灰度化
        BufferedImage grayImage = ImagePixelUtil.convertToGray(image);
        assertNotNull("Gray image should not be null", grayImage);

        // 测试获取灰度值
        int grayValue = ImagePixelUtil.getGrayValue(image, 10, 10);
        assertTrue("Gray value should be between 0 and 255", grayValue >= 0 && grayValue <= 255);

        // 测试像素相似度（相同图片）
        double similarity1 = ImagePixelUtil.calculatePixelSimilarity(image, image);
        assertEquals("Similarity of same images should be 1.0", 1.0, similarity1, 0.001);

        // 测试像素相似度（不同尺寸图片）
        BufferedImage smallImage = createTestImage(50, 50, Color.RED);
        double similarity2 = ImagePixelUtil.calculatePixelSimilarityWithResize(image, smallImage);
        assertTrue("Similarity should be between 0 and 1", similarity2 >= 0.0 && similarity2 <= 1.0);

        System.out.println("ImagePixelUtil smoke test passed!");
    }
}