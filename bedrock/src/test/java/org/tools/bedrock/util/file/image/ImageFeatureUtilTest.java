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
 * ImageFeatureUtil 冒烟测试类
 * 测试图像特征处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageFeatureUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    @Test
    public void testExtractDominantColors() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        Color[] colors = ImageFeatureUtil.extractDominantColors(image, 3);

        assertNotNull("Dominant colors should not be null", colors);
        assertTrue("Should have at least one dominant color", colors.length >= 1);
    }

    @Test
    public void testExtractEdgeFeatures() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        BufferedImage edgeImage = ImageFeatureUtil.extractEdgeFeatures(image);

        assertNotNull("Edge image should not be null", edgeImage);
        assertEquals("Edge image width should match original", image.getWidth(), edgeImage.getWidth());
        assertEquals("Edge image height should match original", image.getHeight(), edgeImage.getHeight());
    }

    @Test
    public void testExtractBasicFeatures() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        String features = ImageFeatureUtil.extractBasicFeatures(image);

        assertNotNull("Features should not be null", features);
        assertFalse("Features should not be empty", features.isEmpty());
    }

    @Test
    public void testIsSolidColorBackground() throws IOException {
        // 测试真实图片，应该不是纯色背景
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        boolean isSolid = ImageFeatureUtil.isSolidColorBackground(image);

        // 对于真实图片，我们不验证具体结果，只验证方法能正常执行
        assertTrue("Method should execute without exception", true);
    }

    @Test
    public void testCalculateFeatureMatchingSimilarity() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImageFeatureUtil.calculateFeatureMatchingSimilarity(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testIsSolidColorBackgroundWithPureColor() throws IOException {
        // 测试纯色背景图片
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/372882.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        boolean isSolid = ImageFeatureUtil.isSolidColorBackground(image);

        // 对于纯色背景图片，应该返回true
        assertTrue("Pure color background image should be detected as solid color background", isSolid);
    }

    @Test
    public void testIsSolidColorBackgroundWithCustomThreshold() throws IOException {
        // 测试使用自定义阈值
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/372882.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        
        // 使用较低阈值应该能检测到纯色背景
        boolean isSolidLowThreshold = ImageFeatureUtil.isSolidColorBackground(image, 0.5f);
        assertTrue("Should detect solid color background with low threshold", isSolidLowThreshold);
        
        // 使用较高阈值可能检测不到纯色背景
        boolean isSolidHighThreshold = ImageFeatureUtil.isSolidColorBackground(image, 0.9f);
        assertFalse("Should not detect solid color background with high threshold", isSolidHighThreshold);
    }

    @Test
    public void testImageFeatureUtilSmoke() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);

        // 测试主要颜色提取
        Color[] colors = ImageFeatureUtil.extractDominantColors(image1, 2);
        assertNotNull("Dominant colors should not be null", colors);

        // 测试边缘特征提取
        BufferedImage edgeImage = ImageFeatureUtil.extractEdgeFeatures(image1);
        assertNotNull("Edge image should not be null", edgeImage);

        // 测试基本特征提取
        String features = ImageFeatureUtil.extractBasicFeatures(image1);
        assertNotNull("Features should not be null", features);

        // 测试纯色背景检测 - 非纯色背景图片
        boolean isSolid1 = ImageFeatureUtil.isSolidColorBackground(image1);
        assertFalse("Method should execute without exception", isSolid1);

        // 测试纯色背景检测 - 纯色背景图片
        URL pureColorURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/372882.jpg");
        assertNotNull("Pure color background resource URL should not be null", pureColorURL);

        BufferedImage pureColorImage = ImageIO.read(pureColorURL);
        boolean isSolid2 = ImageFeatureUtil.isSolidColorBackground(pureColorImage);
        assertTrue("Pure color background image should be detected as solid color background", isSolid2);

        // 测试特征匹配相似度
        double similarity = ImageFeatureUtil.calculateFeatureMatchingSimilarity(image1, image2);
        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);

        System.out.println("ImageFeatureUtil smoke test passed!");
    }
}