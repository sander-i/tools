package org.tools.bedrock.base.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图像相似度结果类
 *
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageSimilarityResult {

    /** 直方图相似度 */
    private double histogramSimilarity;

    /** SSIM（结构相似性）相似度 */
    private double ssimSimilarity;

    /** 平均哈希相似度 */
    private double avgHashSimilarity;

    /** 感知哈希相似度 */
    private double pHashSimilarity;

    /** 差异哈希相似度 */
    private double dHashSimilarity;

    /** 特征匹配相似度 */
    private double featureMatchingSimilarity;

    /** 特征向量相似度 */
    private double featureVectorSimilarity;

    /**
     * 计算综合相似度（加权平均）
     *
     * @return 综合相似度值
     */
    public double getOverallSimilarity() {
        // 加权平均计算综合相似度
        return (histogramSimilarity * 0.15 +
                ssimSimilarity * 0.20 +
                avgHashSimilarity * 0.10 +
                pHashSimilarity * 0.20 +
                dHashSimilarity * 0.10 +
                featureMatchingSimilarity * 0.15 +
                featureVectorSimilarity * 0.10);
    }

    /** 输出图像相似度结果摘要字符串 */
    @Override
    public String toString() {
        return String.format(
                "ImageSimilarityResult{\n" +
                        "  直方图相似度=%.4f,\n" +
                        "  SSIM相似度=%.4f,\n" +
                        "  平均哈希相似度=%.4f,\n" +
                        "  感知哈希相似度=%.4f,\n" +
                        "  差异哈希相似度=%.4f,\n" +
                        "  特征匹配相似度=%.4f,\n" +
                        "  特征向量相似度=%.4f,\n" +
                        "  综合相似度=%.4f\n" +
                        "}",
                histogramSimilarity, ssimSimilarity, avgHashSimilarity,
                pHashSimilarity, dHashSimilarity, featureMatchingSimilarity,
                featureVectorSimilarity, getOverallSimilarity()
        );
    }
}
