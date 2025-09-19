package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.base.image.SimilarityConfig;
import org.tools.bedrock.base.image.SimilarityResult;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * ImageUtil 冒烟测试类
 * 测试图像处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    @Test
    public void testResizeImage() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");

        // 检查资源是否存在
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage originalImage = ImageIO.read(resourceURL);
        
        // 调整图片大小
        BufferedImage resizedImage = ImageUtil.resizeImage(originalImage, 50, 50);
        
        // 验证调整后的图片尺寸
        assertNotNull(resizedImage);
        assertEquals(50, resizedImage.getWidth());
        assertEquals(50, resizedImage.getHeight());
    }

    @Test
    public void testCalculateFusedSimilarityWithSameImage() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        
        // 检查资源是否存在
        assertNotNull("Resource URL should not be null", resourceURL);
        
        // 使用相同图片验证
        BufferedImage image1 = ImageIO.read(resourceURL);
        BufferedImage image2 = ImageIO.read(resourceURL);

        // 计算融合相似度
        SimilarityResult result = ImageUtil.calculateFusedSimilarity(image1, image2);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getIndividualSimilarities());
        assertTrue(result.getOverallSimilarity() >= 0.9); // 相同图片相似度应该很高
    }

    @Test
    public void testCalculateFusedSimilarityWithDifferentImages() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/378991.jpg");
        
        // 计算融合相似度
        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);

        SimilarityResult result = ImageUtil.calculateFusedSimilarity(image1, image2);
        
        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getIndividualSimilarities());
        assertTrue(result.getOverallSimilarity() >= 0); // 相似度应该在0-1之间
        assertTrue(result.getOverallSimilarity() <= 1);
    }

    @Test
    public void testCalculateFusedSimilarityWithCustomConfig() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        
        // 创建自定义配置
        SimilarityConfig config = new SimilarityConfig();
        config.setPixelMatchingEnabled(true);
        config.setHistogramEnabled(true);
        config.setSsimEnabled(false); // 禁用SSIM
        
        // 计算融合相似度
        SimilarityResult result = ImageUtil.calculateFusedSimilarity(image1, image2, config);
        
        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getIndividualSimilarities());
        assertTrue(result.getOverallSimilarity() >= 0.9); // 相同图片相似度应该很高
    }

    @Test
    public void testCalculateFusedSimilarityWithDefaultConfig() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        
        // 使用默认配置计算融合相似度
        SimilarityResult result = ImageUtil.calculateFusedSimilarity(image1, image2);
        
        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getIndividualSimilarities());
        assertTrue(result.getOverallSimilarity() >= 0.9); // 相同图片相似度应该很高
    }
}