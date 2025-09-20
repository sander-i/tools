package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * ImageHistogramUtil 冒烟测试类
 * 测试图像直方图处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageHistogramUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    @Test
    public void testCalculateSimilarity() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImageHistogramUtil.calculateSimilarity(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testCalculateSimilarityWithSameImage() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        double similarity = ImageHistogramUtil.calculateSimilarity(image, image);

        assertTrue("Similarity of same images should be high", similarity >= 0.9);
    }

    @Test
    public void testImageHistogramUtilSmoke() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);

        // 测试相同图片的直方图相似度
        double similarity1 = ImageHistogramUtil.calculateSimilarity(image1, image1);
        assertTrue("Similarity of same images should be high", similarity1 >= 0.9);

        // 测试不同图片的直方图相似度
        double similarity2 = ImageHistogramUtil.calculateSimilarity(image1, image2);
        assertTrue("Similarity should be between 0 and 1", similarity2 >= 0.0 && similarity2 <= 1.0);

        System.out.println("ImageHistogramUtil smoke test passed!");
    }
}