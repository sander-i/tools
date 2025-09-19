package org.tools.bedrock.util.file.image;

import org.tools.bedrock.base.image.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图像特征处理工具类，提供图像特征提取、特征匹配等功能
 *
 * @author sander
 * @version 1.0
 */
public class ImageFeatureUtil {

    /**
     * 提取图片的主要颜色特征
     *
     * @param image 图片
     * @param count 需要返回的主要颜色数量
     * @return 主要颜色数组
     */
    public static Color[] extractDominantColors(BufferedImage image, int count) {
        // 缩小图片以提高处理速度
        BufferedImage resizedImage = ImageUtil.resizeImage(image, 150, 150);

        // 简化的主颜色提取算法
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();

        // 使用通用方法获取所有像素颜色
        int[] colors = new int[width * height];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colors[index++] = resizedImage.getRGB(x, y);
            }
        }

        // 简化处理：返回平均颜色和几个极端颜色
        Color[] dominantColors = new Color[Math.min(count, 5)];
        dominantColors[0] = calculateAverageColor(colors);

        if (count > 1) {
            dominantColors[1] = getBrightestColor(colors);
        }
        if (count > 2) {
            dominantColors[2] = getDarkestColor(colors);
        }
        if (count > 3) {
            dominantColors[3] = getMostRedColor(colors);
        }
        if (count > 4) {
            dominantColors[4] = getMostBlueColor(colors);
        }

        return dominantColors;
    }

    /**
     * 计算平均颜色
     *
     * @param colors 颜色数组
     * @return 平均颜色
     */
    private static Color calculateAverageColor(int[] colors) {
        long red = 0, green = 0, blue = 0;
        for (int color : colors) {
            Color c = new Color(color);
            red += c.getRed();
            green += c.getGreen();
            blue += c.getBlue();
        }
        int pixelCount = colors.length;
        return new Color((int) (red / pixelCount), (int) (green / pixelCount), (int) (blue / pixelCount));
    }

    /**
     * 获取最亮的颜色
     *
     * @param colors 颜色数组
     * @return 最亮的颜色
     */
    private static Color getBrightestColor(int[] colors) {
        Color brightest = new Color(colors[0]);
        float maxBrightness = Color.RGBtoHSB(brightest.getRed(), brightest.getGreen(), brightest.getBlue(), null)[2];

        for (int color : colors) {
            Color c = new Color(color);
            float brightness = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[2];
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
                brightest = c;
            }
        }
        return brightest;
    }

    /**
     * 获取最暗的颜色
     *
     * @param colors 颜色数组
     * @return 最暗的颜色
     */
    private static Color getDarkestColor(int[] colors) {
        Color darkest = new Color(colors[0]);
        float minBrightness = Color.RGBtoHSB(darkest.getRed(), darkest.getGreen(), darkest.getBlue(), null)[2];

        for (int color : colors) {
            Color c = new Color(color);
            float brightness = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[2];
            if (brightness < minBrightness) {
                minBrightness = brightness;
                darkest = c;
            }
        }
        return darkest;
    }

    /**
     * 获取红色分量最多的颜色
     *
     * @param colors 颜色数组
     * @return 红色分量最多的颜色
     */
    private static Color getMostRedColor(int[] colors) {
        Color mostRed = new Color(colors[0]);
        int maxRed = mostRed.getRed();

        for (int color : colors) {
            Color c = new Color(color);
            if (c.getRed() > maxRed) {
                maxRed = c.getRed();
                mostRed = c;
            }
        }
        return mostRed;
    }

    /**
     * 获取蓝色分量最多的颜色
     *
     * @param colors 颜色数组
     * @return 蓝色分量最多的颜色
     */
    private static Color getMostBlueColor(int[] colors) {
        Color mostBlue = new Color(colors[0]);
        int maxBlue = mostBlue.getBlue();

        for (int color : colors) {
            Color c = new Color(color);
            if (c.getBlue() > maxBlue) {
                maxBlue = c.getBlue();
                mostBlue = c;
            }
        }
        return mostBlue;
    }

    /**
     * 提取图片边缘特征
     *
     * @param image 图片
     * @return 边缘特征图
     */
    public static BufferedImage extractEdgeFeatures(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // 简单的边缘检测算法（Sobel算子的简化版）
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // 计算X方向梯度
                int gx = -getGrayValue(image, x - 1, y - 1) - 2 * getGrayValue(image, x - 1, y)
                        - getGrayValue(image, x - 1, y + 1) + getGrayValue(image, x + 1, y - 1)
                        + 2 * getGrayValue(image, x + 1, y) + getGrayValue(image, x + 1, y + 1);

                // 计算Y方向梯度
                int gy = -getGrayValue(image, x - 1, y - 1) - 2 * getGrayValue(image, x, y - 1)
                        - getGrayValue(image, x + 1, y - 1) + getGrayValue(image, x - 1, y + 1)
                        + 2 * getGrayValue(image, x, y + 1) + getGrayValue(image, x + 1, y + 1);

                // 计算梯度幅值
                int gradient = (int) Math.sqrt(gx * gx + gy * gy);
                gradient = Math.min(255, gradient); // 限制在0-255范围内

                // 设置边缘图像的像素值
                int gray = 255 - gradient; // 反转颜色以突出边缘
                edgeImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }

        return edgeImage;
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
     * 提取图片基本特征信息
     *
     * @param image 图片
     * @return 特征信息描述
     */
    public static String extractBasicFeatures(BufferedImage image) {
        StringBuilder features = new StringBuilder();

        features.append("图片尺寸: ").append(image.getWidth()).append("x").append(image.getHeight()).append("\n");
        features.append("色彩模型: ").append(image.getColorModel().toString()).append("\n");

        // 计算平均颜色
        int width = image.getWidth();
        int height = image.getHeight();
        int[] colors = new int[width * height];

        // 使用通用方法获取所有像素颜色
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colors[index++] = image.getRGB(x, y);
            }
        }

        Color avgColor = calculateAverageColor(colors);
        features.append("平均颜色: RGB(")
                .append(avgColor.getRed()).append(",")
                .append(avgColor.getGreen()).append(",")
                .append(avgColor.getBlue()).append(")\n");

        // 计算亮度信息
        BufferedImage grayImage = convertToGray(image);
        int totalBrightness = 0;
        for (int y = 0; y < grayImage.getHeight(); y++) {
            for (int x = 0; x < grayImage.getWidth(); x++) {
                Color color = new Color(grayImage.getRGB(x, y));
                totalBrightness += color.getRed();
            }
        }
        double avgBrightness = (double) totalBrightness / (grayImage.getWidth() * grayImage.getHeight());
        features.append("平均亮度: ").append(String.format("%.2f", avgBrightness)).append("\n");

        return features.toString();
    }

    /**
     * 提取图片基本特征信息
     *
     * @param imagePath 图片路径
     * @return 特征信息描述
     * @throws IOException IO异常
     */
    public static String extractBasicFeatures(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return extractBasicFeatures(image);
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

    /**
     * 计算图片平均颜色（直接从BufferedImage计算）
     *
     * @param image 图片
     * @return 平均颜色
     */
    private static Color calculateAverageColor(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        long red = 0, green = 0, blue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
            }
        }

        int pixelCount = width * height;
        return new Color((int) (red / pixelCount), (int) (green / pixelCount), (int) (blue / pixelCount));
    }

    /**
     * 判断图片是否为纯色背景
     *
     * @param image 图片
     * @return 是否为纯色背景
     */
    public static boolean isSolidColorBackground(BufferedImage image) {
        return isSolidColorBackground(image, 0.65f); // 默认阈值为65%
    }

    /**
     * 判断图片是否为纯色背景
     *
     * @param image     图片
     * @param threshold 阈值（0-1之间），表示主要颜色占比超过该值则认为是纯色背景
     * @return 是否为纯色背景
     */
    public static boolean isSolidColorBackground(BufferedImage image, float threshold) {
        if (threshold <= 0 || threshold > 1) {
            throw new IllegalArgumentException("阈值必须在(0, 1]范围内");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int totalPixels = width * height;

        // 处理边界情况
        if (totalPixels == 0) {
            return false;
        }

        // 对于大图片采用采样方式提高效率
        if (totalPixels > 10000) { // 图片超过10000像素时进行采样
            return isSolidColorBackgroundWithSampling(image, threshold);
        }

        // 直接统计所有像素
        Map<Integer, Integer> colorCountMap = new HashMap<>();
        
        // 使用通用方法进行完整统计
        populateColorCountMap(image, colorCountMap, width, height, 1);

        // 计算满足阈值所需的最小像素数，避免在循环中进行浮点运算
        int thresholdCount = (int) (threshold * totalPixels);

        // 查找出现次数最多的颜色，并检查是否满足阈值条件
        int maxCount = findMaxColorCount(colorCountMap);
        return maxCount >= thresholdCount;
    }

    /**
     * 填充颜色计数Map
     * 
     * @param image 图片
     * @param colorCountMap 颜色计数Map
     * @param width 图片宽度
     * @param height 图片高度
     * @param sampleInterval 采样间隔（1表示不采样，逐像素处理）
     * @return 处理的像素数量
     */
    private static int populateColorCountMap(BufferedImage image, Map<Integer, Integer> colorCountMap,
                                            int width, int height, int sampleInterval) {
        int pixelCount = 0;
        for (int y = 0; y < height; y += sampleInterval) {
            for (int x = 0; x < width; x += sampleInterval) {
                int color = image.getRGB(x, y);
                colorCountMap.put(color, colorCountMap.getOrDefault(color, 0) + 1);
                pixelCount++;
            }
        }
        return pixelCount;
    }

    /**
     * 通过采样方式判断图片是否为纯色背景（用于大图片优化）
     *
     * @param image     图片
     * @param threshold 阈值
     * @return 是否为纯色背景
     */
    private static boolean isSolidColorBackgroundWithSampling(BufferedImage image, float threshold) {
        int width = image.getWidth();
        int height = image.getHeight();
        int totalPixels = width * height;

        // 计算采样间隔，确保采样点数在10000左右
        int sampleInterval = (int) Math.sqrt((double) totalPixels / 10000);
        if (sampleInterval < 1) {
            sampleInterval = 1;
        }

        // 使用通用方法进行采样统计
        Map<Integer, Integer> colorCountMap = new HashMap<>();
        int sampleCount = populateColorCountMap(image, colorCountMap, width, height, sampleInterval);

        // 如果没有采样点，直接返回false
        if (sampleCount == 0) {
            return false;
        }

        // 查找出现次数最多的颜色
        int maxCount = findMaxColorCount(colorCountMap);
        
        // 判断主要颜色占比是否超过阈值
        float ratio = (float) maxCount / sampleCount;
        return ratio >= threshold;
    }
    
    /**
     * 查找颜色计数Map中出现次数最多的颜色的计数值
     * 
     * @param colorCountMap 颜色计数Map
     * @return 最大计数值
     */
    private static int findMaxColorCount(Map<Integer, Integer> colorCountMap) {
        int maxCount = 0;
        for (int count : colorCountMap.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    /**
     * 判断图片是否为纯色背景
     *
     * @param imagePath 图片路径
     * @return 是否为纯色背景
     * @throws IOException IO异常
     */
    public static boolean isSolidColorBackground(String imagePath) throws IOException {
        return isSolidColorBackground(imagePath, 0.65f);
    }

    /**
     * 判断图片是否为纯色背景
     *
     * @param imagePath 图片路径
     * @param threshold 阈值（0-1之间），表示主要颜色占比超过该值则认为是纯色背景
     * @return 是否为纯色背景
     * @throws IOException IO异常
     */
    public static boolean isSolidColorBackground(String imagePath, float threshold) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        return isSolidColorBackground(image, threshold);
    }

    /**
     * 计算两张图片的关键点特征匹配相似度
     * 该方法通过提取图像的关键点和描述符，然后进行匹配来计算相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateFeatureMatchingSimilarity(BufferedImage image1, BufferedImage image2) {
        // 由于Java原生API不支持关键点检测和特征描述符提取，
        // 我们通过模拟方式实现一个简化的特征匹配算法

        // 调整图片大小以提高处理效率
        BufferedImage resizedImage1 = ImageUtil.resizeImage(image1, 100, 100);
        BufferedImage resizedImage2 = ImageUtil.resizeImage(image2, 100, 100);

        // 转换为灰度图
        BufferedImage grayImage1 = convertToGray(resizedImage1);
        BufferedImage grayImage2 = convertToGray(resizedImage2);

        // 提取特征点（简化实现：使用网格点作为特征点）
        List<Point> keypoints1 = extractKeypoints(grayImage1);
        List<Point> keypoints2 = extractKeypoints(grayImage2);

        // 生成描述符（简化实现：使用周围像素值作为描述符）
        Map<Point, int[]> descriptors1 = generateDescriptors(grayImage1, keypoints1);
        Map<Point, int[]> descriptors2 = generateDescriptors(grayImage2, keypoints2);

        // 特征匹配
        int matches = matchFeatures(descriptors1, descriptors2);

        // 计算相似度
        int totalFeatures = Math.max(descriptors1.size(), descriptors2.size());
        if (totalFeatures == 0) {
            return 0.0;
        }

        return (double) matches / totalFeatures;
    }

    /**
     * 计算两张图片的关键点特征匹配相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateFeatureMatchingSimilarity(String imagePath1, String imagePath2) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateFeatureMatchingSimilarity(image1, image2);
    }

    /**
     * 提取图像的关键点（简化实现）
     *
     * @param image 图像
     * @return 关键点列表
     */
    private static List<Point> extractKeypoints(BufferedImage image) {
        List<Point> keypoints = new ArrayList<>();

        // 简化实现：在图像上按网格提取关键点
        int width = image.getWidth();
        int height = image.getHeight();
        int gridSpacing = 10; // 网格间隔

        for (int y = gridSpacing; y < height - gridSpacing; y += gridSpacing) {
            for (int x = gridSpacing; x < width - gridSpacing; x += gridSpacing) {
                // 检查是否为角点（简化实现）
                if (isCorner(image, x, y)) {
                    keypoints.add(new Point(x, y));
                }
            }
        }

        return keypoints;
    }

    /**
     * 检查点是否为角点（简化实现）
     *
     * @param image 图像
     * @param x     x坐标
     * @param y     y坐标
     * @return 是否为角点
     */
    private static boolean isCorner(BufferedImage image, int x, int y) {
        // 简化的角点检测算法
        int center = getGrayValue(image, x, y);
        int threshold = 30; // 角点检测阈值

        // 检查周围像素
        int[] neighbors = new int[8];
        neighbors[0] = getGrayValue(image, x - 1, y - 1);
        neighbors[1] = getGrayValue(image, x, y - 1);
        neighbors[2] = getGrayValue(image, x + 1, y - 1);
        neighbors[3] = getGrayValue(image, x - 1, y);
        neighbors[4] = getGrayValue(image, x + 1, y);
        neighbors[5] = getGrayValue(image, x - 1, y + 1);
        neighbors[6] = getGrayValue(image, x, y + 1);
        neighbors[7] = getGrayValue(image, x + 1, y + 1);

        // 检查是否有足够的差异
        int count = 0;
        for (int neighbor : neighbors) {
            if (Math.abs(neighbor - center) > threshold) {
                count++;
            }
        }

        return count >= 4; // 如果至少有4个邻居像素差异较大，则认为是角点
    }

    /**
     * 生成特征点的描述符（简化实现）
     *
     * @param image     图像
     * @param keypoints 关键点列表
     * @return 描述符映射
     */
    private static Map<Point, int[]> generateDescriptors(BufferedImage image, List<Point> keypoints) {
        Map<Point, int[]> descriptors = new HashMap<>();

        for (Point keypoint : keypoints) {
            // 提取关键点周围的像素值作为描述符
            int[] descriptor = new int[16]; // 4x4区域的像素值
            int index = 0;

            for (int dy = -2; dy <= 1; dy++) {
                for (int dx = -2; dx <= 1; dx++) {
                    int x = keypoint.getX() + dx;
                    int y = keypoint.getY() + dy;

                    if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                        descriptor[index++] = getGrayValue(image, x, y);
                    } else {
                        descriptor[index++] = 0; // 边界外的像素值设为0
                    }
                }
            }

            descriptors.put(keypoint, descriptor);
        }

        return descriptors;
    }

    /**
     * 匹配特征描述符
     *
     * @param descriptors1 描述符1
     * @param descriptors2 描述符2
     * @return 匹配数量
     */
    private static int matchFeatures(Map<Point, int[]> descriptors1, Map<Point, int[]> descriptors2) {
        int matches = 0;
        int threshold = 1000; // 匹配阈值

        for (Map.Entry<Point, int[]> entry1 : descriptors1.entrySet()) {
            int[] descriptor1 = entry1.getValue();
            int minDistance = Integer.MAX_VALUE;
            int[] bestMatch = null;

            // 在描述符2中寻找最近邻
            for (Map.Entry<Point, int[]> entry2 : descriptors2.entrySet()) {
                int[] descriptor2 = entry2.getValue();
                int distance = calculateDescriptorDistance(descriptor1, descriptor2);

                if (distance < minDistance) {
                    minDistance = distance;
                    bestMatch = descriptor2;
                }
            }

            // 如果找到匹配且距离小于阈值，则认为匹配成功
            if (bestMatch != null && minDistance < threshold) {
                matches++;
            }
        }

        return matches;
    }

    /**
     * 计算两个描述符之间的距离
     *
     * @param desc1 描述符1
     * @param desc2 描述符2
     * @return 距离
     */
    private static int calculateDescriptorDistance(int[] desc1, int[] desc2) {
        if (desc1.length != desc2.length) {
            throw new IllegalArgumentException("描述符长度不匹配");
        }

        int distance = 0;
        for (int i = 0; i < desc1.length; i++) {
            distance += Math.abs(desc1[i] - desc2[i]);
        }

        return distance;
    }


    /**
     * 提取图像的特征向量（简化实现）
     *
     * @param image 图像
     * @return 特征向量
     */
    public static double[] extractFeatureVector(BufferedImage image) {
        // 调整图片大小
        BufferedImage resized = ImageUtil.resizeImage(image, 32, 32);

        // 转换为灰度图
        BufferedImage gray = convertToGray(resized);

        // 提取特征向量（简化实现：使用像素值作为特征）
        double[] features = new double[32 * 32];
        int index = 0;

        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                features[index++] = getGrayValue(gray, x, y) / 255.0; // 归一化到0-1之间
            }
        }

        return features;
    }

    /**
     * 计算两个特征向量之间的余弦相似度
     *
     * @param vector1 特征向量1
     * @param vector2 特征向量2
     * @return 余弦相似度（0-1之间，1表示完全相同）
     */
    public static double calculateCosineSimilarity(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("特征向量长度不匹配");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 计算两张图片的特征向量相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度值（0-1之间，1表示完全相同）
     */
    public static double calculateFeatureVectorSimilarity(BufferedImage image1, BufferedImage image2) {
        double[] vector1 = extractFeatureVector(image1);
        double[] vector2 = extractFeatureVector(image2);
        return calculateCosineSimilarity(vector1, vector2);
    }

    /**
     * 计算两张图片的特征向量相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度值（0-1之间，1表示完全相同）
     * @throws IOException IO异常
     */
    public static double calculateFeatureVectorSimilarity(String imagePath1, String imagePath2) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateFeatureVectorSimilarity(image1, image2);
    }

}