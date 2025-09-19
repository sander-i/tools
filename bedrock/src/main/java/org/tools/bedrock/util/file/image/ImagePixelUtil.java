package org.tools.bedrock.util.file.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图像像素处理工具类，提供像素级操作和基础图像处理功能
 *
 * @author sander
 * @version 1.0
 */
public class ImagePixelUtil {

    /**
     * 将图片转换为二值图
     *
     * @param image     原始图片
     * @param threshold 阈值
     * @return 二值图
     */
    public static BufferedImage convertToBinary(BufferedImage image, int threshold) {
        BufferedImage binaryImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g2d = binaryImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return binaryImage;
    }

    /**
     * 获取像素的灰度值
     *
     * @param image 图片
     * @param x     x坐标
     * @param y     y坐标
     * @return 灰度值
     */
    public static int getGrayValue(BufferedImage image, int x, int y) {
        Color color = new Color(image.getRGB(x, y));
        return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
    }

    /**
     * 将图片转换为灰度图
     *
     * @param image 原始图片
     * @return 灰度图
     */
    public static BufferedImage convertToGray(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }

    /**
     * 计算两张图片的像素级相似度
     * 通过比较每个像素点的RGB值来计算相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IllegalArgumentException 当图片尺寸不匹配时抛出异常
     */
    public static double calculatePixelSimilarity(BufferedImage image1, BufferedImage image2) {
        // 检查图片尺寸是否一致
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            throw new IllegalArgumentException("两张图片的尺寸不一致，无法进行像素级比较");
        }

        int width = image1.getWidth();
        int height = image1.getHeight();
        int totalPixels = width * height;

        // 如果图片为空，返回0相似度
        if (totalPixels == 0) {
            return 0.0;
        }

        long totalDifference = 0;
        long maxDifference = 0;

        // 遍历每个像素点计算差异
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = image1.getRGB(x, y);
                int rgb2 = image2.getRGB(x, y);

                // 分别获取RGB分量
                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;

                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;

                // 计算RGB差异
                int dr = Math.abs(r1 - r2);
                int dg = Math.abs(g1 - g2);
                int db = Math.abs(b1 - b2);

                // 累加差异值
                totalDifference += dr + dg + db;

                // 最大可能差异（每个分量最大差异为255）
                maxDifference += 255 * 3;
            }
        }

        // 计算相似度（1 - 差异百分比）
        if (maxDifference == 0) {
            return 1.0;
        }

        double similarity = 1.0 - (double) totalDifference / maxDifference;
        return similarity;
    }

    /**
     * 计算两张图片的像素级相似度（自动调整图片大小）
     * 通过比较每个像素点的RGB值来计算相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculatePixelSimilarityWithResize(BufferedImage image1, BufferedImage image2) {
        int targetWidth = Math.min(image1.getWidth(), image2.getWidth());
        int targetHeight = Math.min(image1.getHeight(), image2.getHeight());

        // 如果任一图片尺寸大于目标尺寸，则调整大小
        if (image1.getWidth() != targetWidth || image1.getHeight() != targetHeight) {
            image1 = resizeImage(image1, targetWidth, targetHeight);
        }

        if (image2.getWidth() != targetWidth || image2.getHeight() != targetHeight) {
            image2 = resizeImage(image2, targetWidth, targetHeight);
        }

        return calculatePixelSimilarity(image1, image2);
    }

    /**
     * 调整图片大小
     *
     * @param originalImage 原始图片
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @return 调整后的图片
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}