package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * ImageHashUtil 冒烟测试类
 * 测试图像哈希处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageHashUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    @Test
    public void testCalculateAverageHash() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        String hash = ImageHashUtil.calculateAverageHash(image);

        assertNotNull("Hash should not be null", hash);
        assertEquals("Average hash should be 64 characters long", 64, hash.length());
    }

    @Test
    public void testCalculatePerceptualHash() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        String hash = ImageHashUtil.calculatePerceptualHash(image);

        assertNotNull("Hash should not be null", hash);
        assertEquals("Perceptual hash should be 64 characters long", 64, hash.length());
    }

    @Test
    public void testCalculateDifferenceHash() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        String hash = ImageHashUtil.calculateDifferenceHash(image);

        assertNotNull("Hash should not be null", hash);
        assertEquals("Difference hash should be 64 characters long", 64, hash.length());
    }

    @Test
    public void testCalculateHammingDistance() {
        String hash1 = "1111111111111111111111111111111111111111111111111111111111111111";
        String hash2 = "0000000000000000000000000000000000000000000000000000000000000000";
        
        int distance = ImageHashUtil.calculateHammingDistance(hash1, hash2);
        assertEquals("Hamming distance should be 64 for completely different hashes", 64, distance);
    }

    @Test
    public void testCalculateAverageHashSimilarity() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImageHashUtil.calculateAverageHashSimilarity(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testCalculatePerceptualHashSimilarity() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImageHashUtil.calculatePerceptualHashSimilarity(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testCalculateDifferenceHashSimilarity() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double similarity = ImageHashUtil.calculateDifferenceHashSimilarity(image1, image2);

        assertTrue("Similarity should be between 0 and 1", similarity >= 0.0 && similarity <= 1.0);
    }

    @Test
    public void testImageHashUtilSmoke() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);

        // 测试平均哈希
        String avgHash1 = ImageHashUtil.calculateAverageHash(image1);
        String avgHash2 = ImageHashUtil.calculateAverageHash(image2);
        assertNotNull("Average hash 1 should not be null", avgHash1);
        assertNotNull("Average hash 2 should not be null", avgHash2);

        // 测试感知哈希
        String pHash1 = ImageHashUtil.calculatePerceptualHash(image1);
        String pHash2 = ImageHashUtil.calculatePerceptualHash(image2);
        assertNotNull("Perceptual hash 1 should not be null", pHash1);
        assertNotNull("Perceptual hash 2 should not be null", pHash2);

        // 测试差异哈希
        String dHash1 = ImageHashUtil.calculateDifferenceHash(image1);
        String dHash2 = ImageHashUtil.calculateDifferenceHash(image2);
        assertNotNull("Difference hash 1 should not be null", dHash1);
        assertNotNull("Difference hash 2 should not be null", dHash2);

        // 测试汉明距离
        int distance = ImageHashUtil.calculateHammingDistance(avgHash1, avgHash2);
        assertTrue("Hamming distance should be non-negative", distance >= 0);

        // 测试各种哈希相似度计算
        double avgSimilarity = ImageHashUtil.calculateAverageHashSimilarity(image1, image2);
        double pSimilarity = ImageHashUtil.calculatePerceptualHashSimilarity(image1, image2);
        double dSimilarity = ImageHashUtil.calculateDifferenceHashSimilarity(image1, image2);

        assertTrue("Average hash similarity should be between 0 and 1", avgSimilarity >= 0.0 && avgSimilarity <= 1.0);
        assertTrue("Perceptual hash similarity should be between 0 and 1", pSimilarity >= 0.0 && pSimilarity <= 1.0);
        assertTrue("Difference hash similarity should be between 0 and 1", dSimilarity >= 0.0 && dSimilarity <= 1.0);

        System.out.println("ImageHashUtil smoke test passed!");
    }
}