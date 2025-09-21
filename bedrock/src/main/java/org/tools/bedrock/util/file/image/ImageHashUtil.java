package org.tools.bedrock.util.file.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像哈希处理工具类，提供平均哈希、感知哈希、差异哈希等算法实现
 *
 * @author sander
 * @version 1.0
 */
public class ImageHashUtil {

    private ImageHashUtil() {
    }

    /**
     * 计算图片的平均哈希值(Average Hash)
     *
     * @param image 图片
     * @return 哈希值字符串
     */
    public static String calculateAverageHash(BufferedImage image) {
        // 缩放图片到8x8大小
        BufferedImage resized = ImageUtil.resizeImage(image, 8, 8);

        // 转换为灰度图
        BufferedImage gray = convertToGray(resized);

        // 计算平均值
        int total = 0;
        int[] pixels = new int[64];
        int index = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int pixel = getGrayValue(gray, x, y);
                pixels[index++] = pixel;
                total += pixel;
            }
        }

        int average = total / 64;

        // 生成哈希值
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            hash.append(pixels[i] >= average ? "1" : "0");
        }

        return hash.toString();
    }

    /**
     * 计算图片的平均哈希值(Average Hash)
     *
     * @param imagePath 图片路径
     * @return 哈希值字符串
     * @throws IOException IO异常
     */
    public static String calculateAverageHash(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return calculateAverageHash(image);
    }

    /**
     * 计算图片的感知哈希值(Perceptual Hash)
     *
     * @param image 图片
     * @return 哈希值字符串
     */
    public static String calculatePerceptualHash(BufferedImage image) {
        // 缩放图片到32x32大小
        BufferedImage resized = ImageUtil.resizeImage(image, 32, 32);

        // 转换为灰度图
        BufferedImage gray = convertToGray(resized);

        // 计算DCT(离散余弦变换)的简化版本
        double[][] dct = new double[32][32];
        double[][] matrix = new double[32][32];

        // 填充矩阵
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                matrix[y][x] = getGrayValue(gray, x, y);
            }
        }

        // 简化的DCT计算（仅计算左上角8x8区域）
        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                double sum = 0.0;
                for (int y = 0; y < 32; y++) {
                    for (int x = 0; x < 32; x++) {
                        double cosU = Math.cos((2 * x + 1) * u * Math.PI / 64);
                        double cosV = Math.cos((2 * y + 1) * v * Math.PI / 64);
                        sum += matrix[y][x] * cosU * cosV;
                    }
                }
                dct[u][v] = sum;
            }
        }

        // 取左上角8x8区域的DCT系数
        double[][] dctReduced = new double[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(dct[i], 0, dctReduced[i], 0, 8);
        }

        // 计算平均值（排除DC系数）
        double total = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i != 0 || j != 0) { // 排除DC系数
                    total += dctReduced[i][j];
                }
            }
        }
        double average = total / 63;

        // 生成哈希值
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                hash.append(dctReduced[i][j] > average ? "1" : "0");
            }
        }

        return hash.toString();
    }

    /**
     * 计算图片的感知哈希值(Perceptual Hash)
     *
     * @param imagePath 图片路径
     * @return 哈希值字符串
     * @throws IOException IO异常
     */
    public static String calculatePerceptualHash(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return calculatePerceptualHash(image);
    }

    /**
     * 计算图片的差异哈希值(Difference Hash)
     *
     * @param image 图片
     * @return 哈希值字符串
     */
    public static String calculateDifferenceHash(BufferedImage image) {
        // 缩放图片到9x8大小
        BufferedImage resized = ImageUtil.resizeImage(image, 9, 8);

        // 转换为灰度图
        BufferedImage gray = convertToGray(resized);

        // 生成哈希值
        StringBuilder hash = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int left = getGrayValue(gray, x, y);
                int right = getGrayValue(gray, x + 1, y);
                hash.append(left > right ? "1" : "0");
            }
        }

        return hash.toString();
    }

    /**
     * 计算图片的差异哈希值(Difference Hash)
     *
     * @param imagePath 图片路径
     * @return 哈希值字符串
     * @throws IOException IO异常
     */
    public static String calculateDifferenceHash(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return calculateDifferenceHash(image);
    }

    /**
     * 计算两个哈希值之间的汉明距离
     *
     * @param hash1 哈希值1
     * @param hash2 哈希值2
     * @return 汉明距离（不同位的数量）
     */
    public static int calculateHammingDistance(String hash1, String hash2) {
        if (hash1.length() != hash2.length()) {
            throw new IllegalArgumentException("哈希值长度不匹配");
        }

        int distance = 0;
        for (int i = 0; i < hash1.length(); i++) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }

    /**
     * 基于哈希值计算图片相似度
     *
     * @param image1   第一张图片
     * @param image2   第二张图片
     * @param hashType 哈希算法类型(1: Average Hash, 2: Perceptual Hash, 3: Difference Hash)
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateHashSimilarity(BufferedImage image1, BufferedImage image2, int hashType) {
        String hash1, hash2;

        switch (hashType) {
            case 1:
                hash1 = calculateAverageHash(image1);
                hash2 = calculateAverageHash(image2);
                break;
            case 2:
                hash1 = calculatePerceptualHash(image1);
                hash2 = calculatePerceptualHash(image2);
                break;
            case 3:
                hash1 = calculateDifferenceHash(image1);
                hash2 = calculateDifferenceHash(image2);
                break;
            default:
                throw new IllegalArgumentException("无效的哈希类型，支持的类型: 1(Average Hash), 2(Perceptual Hash), 3(Difference Hash)");
        }

        int distance = calculateHammingDistance(hash1, hash2);
        int hashLength = hash1.length();

        // 转换为相似度（0-1之间）
        return 1.0 - (double) distance / hashLength;
    }

    /**
     * 基于哈希值计算图片相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @param hashType   哈希算法类型(1: Average Hash, 2: Perceptual Hash, 3: Difference Hash)
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateHashSimilarity(String imagePath1, String imagePath2, int hashType) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateHashSimilarity(image1, image2, hashType);
    }

    /**
     * 基于平均哈希值计算图片相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateAverageHashSimilarity(BufferedImage image1, BufferedImage image2) {
        return calculateHashSimilarity(image1, image2, 1);
    }

    /**
     * 基于平均哈希值计算图片相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateAverageHashSimilarity(String imagePath1, String imagePath2) throws IOException {
        return calculateHashSimilarity(imagePath1, imagePath2, 1);
    }

    /**
     * 基于感知哈希值计算图片相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculatePerceptualHashSimilarity(BufferedImage image1, BufferedImage image2) {
        return calculateHashSimilarity(image1, image2, 2);
    }

    /**
     * 基于感知哈希值计算图片相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculatePerceptualHashSimilarity(String imagePath1, String imagePath2) throws IOException {
        return calculateHashSimilarity(imagePath1, imagePath2, 2);
    }

    /**
     * 基于差异哈希值计算图片相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateDifferenceHashSimilarity(BufferedImage image1, BufferedImage image2) {
        return calculateHashSimilarity(image1, image2, 3);
    }

    /**
     * 基于差异哈希值计算图片相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateDifferenceHashSimilarity(String imagePath1, String imagePath2) throws IOException {
        return calculateHashSimilarity(imagePath1, imagePath2, 3);
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