package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ImageStructureUtil 冒烟测试类
 * 测试图像结构处理工具类的基本功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageStructureUtilTest {

    private static final String TEST_RESOURCE_IMAGE_PATH = "static";

    @Test
    public void testCalculateSSIM() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);
        double ssim = ImageStructureUtil.calculateSSIM(image1, image2);

        assertTrue("SSIM should be between 0 and 1", ssim >= 0.0 && ssim <= 1.0);
    }

    @Test
    public void testCalculateSSIMWithSameImage() throws IOException {
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        BufferedImage image = ImageIO.read(resourceURL);
        double ssim = ImageStructureUtil.calculateSSIM(image, image);

        assertTrue("SSIM of same images should be high", ssim >= 0.9);
    }

    @Test
    public void testImageStructureUtilSmoke() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

        BufferedImage image1 = ImageIO.read(resourceURL1);
        BufferedImage image2 = ImageIO.read(resourceURL2);

        // 测试相同图片的SSIM
        double ssim1 = ImageStructureUtil.calculateSSIM(image1, image1);
        assertTrue("SSIM of same images should be high", ssim1 >= 0.9);

        // 测试不同图片的SSIM
        double ssim2 = ImageStructureUtil.calculateSSIM(image1, image2);
        assertTrue("SSIM should be between 0 and 1", ssim2 >= 0.0 && ssim2 <= 1.0);

        System.out.println("ImageStructureUtil smoke test passed!");
    }
}