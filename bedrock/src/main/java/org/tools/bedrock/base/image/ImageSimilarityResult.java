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
    private double histogramSimilarity;
    private double ssimSimilarity;
    private double avgHashSimilarity;
    private double pHashSimilarity;
    private double dHashSimilarity;
    private double featureMatchingSimilarity;
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