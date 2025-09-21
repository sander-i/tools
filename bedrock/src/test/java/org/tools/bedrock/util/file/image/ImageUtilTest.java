package org.tools.bedrock.util.file.image;

import org.junit.Test;
import org.tools.bedrock.base.image.SimilarityConfig;
import org.tools.bedrock.base.image.SimilarityResult;
import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.util.file.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

        BufferedImage image = ImageIO.read(resourceURL);

        // 计算融合相似度（相同图片）
        SimilarityResult result = ImageUtil.calculateFusedSimilarity(image, image);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getIndividualSimilarities());
        assertEquals(1.0, result.getOverallSimilarity(), 0.001); // 相同图片相似度应该为1
    }

    @Test
    public void testCalculateFusedSimilarityWithDifferentImages() throws IOException {
        URL resourceURL1 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        URL resourceURL2 = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391861.jpg");

        // 检查资源是否存在
        assertNotNull("Resource URL 1 should not be null", resourceURL1);
        assertNotNull("Resource URL 2 should not be null", resourceURL2);

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

    @Test
    public void testImageToBinaryLossWithNullPath() {
        // 测试imageToBinaryLoss方法处理null参数
        byte[] result = ImageUtil.imageToBinaryLoss(null);
        assertArrayEquals("传入null路径应返回空字节数组", Constants.EMPTY_BYTE_ARRAY, result);
    }

    @Test
    public void testImageToBinaryLossWithValidImage() throws IOException {
        // 使用实际图像测试imageToBinaryLoss方法
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        String imagePath = resourceURL.getPath();
        // 在Windows系统中，路径可能以/开头，需要去掉
        if (System.getProperty("os.name").toLowerCase().contains("win") && imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }

        byte[] result = ImageUtil.imageToBinaryLoss(imagePath);
        assertNotNull("返回的字节数组不应为null", result);
        assertTrue("返回的字节数组长度应大于0", result.length > 0);
    }

    @Test
    public void testBinaryToImageLossWithNullBytes() {
        // 测试binaryToImageLoss方法处理null参数
        ImageUtil.binaryToImageLoss(null, "test.jpg");
        // 不应抛出异常
    }

    @Test
    public void testBinaryToImageLossWithValidData() throws IOException {
        // 使用实际图像测试binaryToImageLoss方法
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        String imagePath = resourceURL.getPath();
        // 在Windows系统中，路径可能以/开头，需要去掉
        if (System.getProperty("os.name").toLowerCase().contains("win") && imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }

        byte[] imageBytes = ImageUtil.imageToBinaryLoss(imagePath);
        assertNotNull("图像字节数组不应为null", imageBytes);
        assertTrue("图像字节数组长度应大于0", imageBytes.length > 0);

        // 将图像字节写入临时文件
        String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "test_output.jpg";
        ImageUtil.binaryToImageLoss(imageBytes, tempFilePath);

        // 验证文件是否创建成功
        File outputFile = new File(tempFilePath);
        assertTrue("输出文件应存在", outputFile.exists());
        assertTrue("输出文件大小应大于0", outputFile.length() > 0);

        // 清理临时文件
        outputFile.delete();
    }

    @Test
    public void testImageToBinaryWithNullPath() {
        // 测试imageToBinary方法处理null参数
        byte[] result = ImageUtil.imageToBinary(null);
        assertArrayEquals("传入null路径应返回空字节数组", Constants.EMPTY_BYTE_ARRAY, result);
    }

    @Test
    public void testImageToBinaryWithValidImage() throws IOException {
        // 使用实际图像测试imageToBinary方法
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        String imagePath = resourceURL.getPath();
        // 在Windows系统中，路径可能以/开头，需要去掉
        if (System.getProperty("os.name").toLowerCase().contains("win") && imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }

        byte[] result = ImageUtil.imageToBinary(imagePath);
        assertNotNull("返回的字节数组不应为null", result);
        assertTrue("返回的字节数组长度应大于0", result.length > 0);
    }

    @Test
    public void testBinaryToImageWithNullBytes() {
        // 测试binaryToImage方法处理null参数
        ImageUtil.binaryToImage(null, "test.jpg");
        // 不应抛出异常
    }

    @Test
    public void testBinaryToImageWithValidData() throws IOException {
        // 使用实际图像测试binaryToImage方法
        URL resourceURL = FileUtil.getResourceURL(TEST_RESOURCE_IMAGE_PATH + "/391860.jpg");
        assertNotNull("Resource URL should not be null", resourceURL);

        String imagePath = resourceURL.getPath();
        // 在Windows系统中，路径可能以/开头，需要去掉
        if (System.getProperty("os.name").toLowerCase().contains("win") && imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }

        byte[] imageBytes = ImageUtil.imageToBinary(imagePath);
        assertNotNull("图像字节数组不应为null", imageBytes);
        assertTrue("图像字节数组长度应大于0", imageBytes.length > 0);

        // 将图像字节写入临时文件
        String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "test_output2.jpg";
        ImageUtil.binaryToImage(imageBytes, tempFilePath);

        // 验证文件是否创建成功
        File outputFile = new File(tempFilePath);
        assertTrue("输出文件应存在", outputFile.exists());
        assertTrue("输出文件大小应大于0", outputFile.length() > 0);

        // 清理临时文件
        outputFile.delete();
    }
}