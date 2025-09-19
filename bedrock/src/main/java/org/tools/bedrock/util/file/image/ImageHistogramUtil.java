package org.tools.bedrock.util.file.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像直方图处理工具类，提供直方图计算和基于直方图的图像相似度比对功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageHistogramUtil {

    /**
     * 计算两张图片的相似度（基于直方图）
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateSimilarity(BufferedImage image1, BufferedImage image2) {
        // 调整图片大小以提高比较效率
        BufferedImage resizedImage1 = ImageUtil.resizeImage(image1, 100, 100);
        BufferedImage resizedImage2 = ImageUtil.resizeImage(image2, 100, 100);

        // 计算直方图
        int[] histogram1 = calculateHistogram(resizedImage1);
        int[] histogram2 = calculateHistogram(resizedImage2);

        // 计算直方图相似度（使用巴氏距离）
        return calculateHistogramSimilarity(histogram1, histogram2);
    }

    /**
     * 计算两张图片的相似度（基于直方图）
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateSimilarity(String imagePath1, String imagePath2) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateSimilarity(image1, image2);
    }

    /**
     * 计算图片的RGB直方图
     *
     * @param image 图片
     * @return 直方图数组（768个元素：256个红色值 + 256个绿色值 + 256个蓝色值）
     */
    private static int[] calculateHistogram(BufferedImage image) {
        int[] histogram = new int[768];
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                histogram[color.getRed()]++;
                histogram[256 + color.getGreen()]++;
                histogram[512 + color.getBlue()]++;
            }
        }

        return histogram;
    }

    /**
     * 使用巴氏距离计算两个直方图的相似度
     *
     * @param histogram1 直方图1
     * @param histogram2 直方图2
     * @return 相似度值（0-1之间）
     */
    private static double calculateHistogramSimilarity(int[] histogram1, int[] histogram2) {
        double similarity = 0.0;
        double sum1 = 0.0;
        double sum2 = 0.0;

        // 计算直方图总和
        for (int i = 0; i < 768; i++) {
            sum1 += histogram1[i];
            sum2 += histogram2[i];
        }

        // 计算巴氏系数
        for (int i = 0; i < 768; i++) {
            double p = histogram1[i] / sum1;
            double q = histogram2[i] / sum2;
            similarity += Math.sqrt(p * q);
        }

        return similarity;
    }
}