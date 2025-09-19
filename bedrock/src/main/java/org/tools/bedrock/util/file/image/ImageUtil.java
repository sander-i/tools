package org.tools.bedrock.util.file.image;

import org.tools.bedrock.base.image.SimilarityConfig;
import org.tools.bedrock.base.image.SimilarityResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图像处理工具类，提供基础图像操作功能和图像相似度融合计算
 *
 * @author sander
 * @version 1.0
 */
public class ImageUtil {

    /**
     * 调整图片大小
     *
     * @param originalImage 原始图片
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @return 调整后的图片
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    /**
     * 计算两张图片的融合相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @param config 相似度计算配置
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     */
    public static SimilarityResult calculateFusedSimilarity(BufferedImage image1, BufferedImage image2, SimilarityConfig config) {
        Map<String, Double> similarities = new HashMap<>();
        double totalWeight = 0.0;
        double weightedSum = 0.0;

        // 计算像素匹配相似度
        if (config.isPixelMatchingEnabled()) {
            double pixelMatchingSimilarity = ImagePixelUtil.calculatePixelSimilarityWithResize(image1, image2);
            similarities.put("PixelMatching", pixelMatchingSimilarity);
            weightedSum += pixelMatchingSimilarity * config.getPixelMatchingWeight();
            totalWeight += config.getPixelMatchingWeight();
        }

        // 计算直方图相似度
        if (config.isHistogramEnabled()) {
            double histogramSimilarity = ImageHistogramUtil.calculateSimilarity(image1, image2);
            similarities.put("Histogram", histogramSimilarity);
            weightedSum += histogramSimilarity * config.getHistogramWeight();
            totalWeight += config.getHistogramWeight();
        }

        // 计算SSIM相似度
        if (config.isSsimEnabled()) {
            double ssimSimilarity = ImageStructureUtil.calculateSSIM(image1, image2);
            similarities.put("SSIM", ssimSimilarity);
            weightedSum += ssimSimilarity * config.getSsimWeight();
            totalWeight += config.getSsimWeight();
        }

        // 计算哈希相似度
        if (config.isAvgHashEnabled() || config.isPHashEnabled() || config.isDHashEnabled()) {
            Map<String, Double> hashSimilarities = calculateHashSimilarities(image1, image2, config);
            for (Map.Entry<String, Double> entry : hashSimilarities.entrySet()) {
                similarities.put(entry.getKey(), entry.getValue());
                switch (entry.getKey()) {
                    case "AverageHash":
                        weightedSum += entry.getValue() * config.getAvgHashWeight();
                        totalWeight += config.getAvgHashWeight();
                        break;
                    case "PerceptualHash":
                        weightedSum += entry.getValue() * config.getPHashWeight();
                        totalWeight += config.getPHashWeight();
                        break;
                    case "DifferenceHash":
                        weightedSum += entry.getValue() * config.getDHashWeight();
                        totalWeight += config.getDHashWeight();
                        break;
                    default:
                        // 不应该到达这里，因为我们只放入了已知的键
                        break;
                }
            }
        }

        // 计算特征匹配相似度
        if (config.isFeatureMatchingEnabled()) {
            double featureMatchingSimilarity = ImageFeatureUtil.calculateFeatureMatchingSimilarity(image1, image2);
            similarities.put("FeatureMatching", featureMatchingSimilarity);
            weightedSum += featureMatchingSimilarity * config.getFeatureMatchingWeight();
            totalWeight += config.getFeatureMatchingWeight();
        }

        // 计算加权平均相似度
        double overallSimilarity = totalWeight > 0 ? weightedSum / totalWeight : 0.0;

        return new SimilarityResult(similarities, overallSimilarity);
    }

    /**
     * 计算图片的哈希相似度
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @param config 相似度计算配置
     * @return 哈希相似度映射
     */
    private static Map<String, Double> calculateHashSimilarities(BufferedImage image1, BufferedImage image2, SimilarityConfig config) {
        Map<String, Double> hashSimilarities = new HashMap<>();

        // 计算平均哈希相似度
        if (config.isAvgHashEnabled()) {
            double avgHashSimilarity = ImageHashUtil.calculateAverageHashSimilarity(image1, image2);
            hashSimilarities.put("AverageHash", avgHashSimilarity);
        }

        // 计算感知哈希相似度
        if (config.isPHashEnabled()) {
            double pHashSimilarity = ImageHashUtil.calculatePerceptualHashSimilarity(image1, image2);
            hashSimilarities.put("PerceptualHash", pHashSimilarity);
        }

        // 计算差异哈希相似度
        if (config.isDHashEnabled()) {
            double dHashSimilarity = ImageHashUtil.calculateDifferenceHashSimilarity(image1, image2);
            hashSimilarities.put("DifferenceHash", dHashSimilarity);
        }

        return hashSimilarities;
    }

    /**
     * 计算两张图片的融合相似度（使用默认配置）
     *
     * @param image1 第一张图片
     * @param image2 第二张图片
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     */
    public static SimilarityResult calculateFusedSimilarity(BufferedImage image1, BufferedImage image2) {
        return calculateFusedSimilarity(image1, image2, new SimilarityConfig());
    }

    /**
     * 计算两张图片的融合相似度
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @param config     相似度计算配置
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     * @throws IOException IO异常
     */
    public static SimilarityResult calculateFusedSimilarity(String imagePath1, String imagePath2, SimilarityConfig config) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(imagePath1));
        BufferedImage image2 = ImageIO.read(new File(imagePath2));
        return calculateFusedSimilarity(image1, image2, config);
    }

    /**
     * 计算两张图片的融合相似度（使用默认配置）
     *
     * @param imagePath1 第一张图片路径
     * @param imagePath2 第二张图片路径
     * @return 相似度结果，包含各项相似度值和加权平均后的总体相似度
     * @throws IOException IO异常
     */
    public static SimilarityResult calculateFusedSimilarity(String imagePath1, String imagePath2) throws IOException {
        return calculateFusedSimilarity(imagePath1, imagePath2, new SimilarityConfig());
    }

}