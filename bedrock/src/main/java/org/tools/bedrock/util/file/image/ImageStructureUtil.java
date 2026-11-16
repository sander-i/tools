package org.tools.bedrock.util.file.image;

import org.tools.bedrock.constant.ImageConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像结构处理工具类，提供结构相似度(SSIM)计算功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageStructureUtil {

    private ImageStructureUtil() {
    }

    /**
     * 计算两张图片的结构相似度(SSIM)
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 结构相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateSSIM(BufferedImage image1, BufferedImage image2) {
        // 调整图片大小以提高计算效率
        BufferedImage resizedImage1 = ImageUtil.resizeImage(image1, 200, 200);
        BufferedImage resizedImage2 = ImageUtil.resizeImage(image2, 200, 200);

        // 转换为灰度图
        BufferedImage grayImage1 = convertToGray(resizedImage1);
        BufferedImage grayImage2 = convertToGray(resizedImage2);

        // 计算SSIM
        return computeSSIM(grayImage1, grayImage2);
    }

    /**
     * 计算两张图片的结构相似度(SSIM)
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 结构相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateSSIM(String imagePath1, String imagePath2) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateSSIM(image1, image2);
    }

    /**
     * 计算两张灰度图的SSIM值
     *
     * @param img1 灰度图1
     * @param img2 灰度图2
     * @return SSIM值
     */
    private static double computeSSIM(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();

        // SSIM参数
        final double K1 = ImageConstants.SSIM_K1;
        final double K2 = ImageConstants.SSIM_K2;
        final int L = ImageConstants.SSIM_L; // 8位图像的像素值范围
        final double C1 = Math.pow(K1 * L, 2);
        final double C2 = Math.pow(K2 * L, 2);

        // 计算均值
        double mean1 = 0, mean2 = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mean1 += getGrayValue(img1, x, y);
                mean2 += getGrayValue(img2, x, y);
            }
        }
        mean1 /= (width * height);
        mean2 /= (width * height);

        // 计算方差和协方差
        double variance1 = 0, variance2 = 0, covariance = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = getGrayValue(img1, x, y);
                int pixel2 = getGrayValue(img2, x, y);

                variance1 += Math.pow(pixel1 - mean1, 2);
                variance2 += Math.pow(pixel2 - mean2, 2);
                covariance += (pixel1 - mean1) * (pixel2 - mean2);
            }
        }
        variance1 /= (width * height - 1);
        variance2 /= (width * height - 1);
        covariance /= (width * height - 1);

        // 计算SSIM
        double numerator = (2 * mean1 * mean2 + C1) * (2 * covariance + C2);
        double denominator = (Math.pow(mean1, 2) + Math.pow(mean2, 2) + C1) *
                (variance1 + variance2 + C2);

        return numerator / denominator;
    }

    /**
     * 获取像素的灰度值
     *
     * @param image 图片
     * @param x     x坐标
     * @param y     y坐标
     * @return 灰度值
     */
    private static int getGrayValue(BufferedImage image, int x, int y) {
        Color color = new Color(image.getRGB(x, y));
        return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
    }

    /**
     * 将图片转换为灰度图
     *
     * @param image 原始图片
     * @return 灰度图
     */
    private static BufferedImage convertToGray(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }
}