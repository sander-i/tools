package org.tools.bedrock.base.image;

import lombok.Data;

/**
 * 图像相似度融合计算方法配置类
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@Data
public class SimilarityConfig {
    /**
     * 是否启用像素匹配维度
     */
    private boolean pixelMatchingEnabled = true;

    /**
     * 像素匹配权重
     */
    private double pixelMatchingWeight = 0.15;

    /**
     * 是否启用直方图维度
     */
    private boolean histogramEnabled = true;

    /**
     * 直方图权重
     */
    private double histogramWeight = 0.15;

    /**
     * 是否启用 SSIM 维度
     */
    private boolean ssimEnabled = true;

    /**
     * SSIM 权重
     */
    private double ssimWeight = 0.25;

    /**
     * 是否启用平均哈希维度
     */
    private boolean avgHashEnabled = true;

    /**
     * 平均哈希权重
     */
    private double avgHashWeight = 0.1;

    /**
     * 是否启用感知哈希维度
     */
    private boolean pHashEnabled = true;

    /**
     * 感知哈希权重
     */
    private double pHashWeight = 0.15;

    /**
     * 是否启用差异哈希维度
     */
    private boolean dHashEnabled = true;

    /**
     * 差异哈希权重
     */
    private double dHashWeight = 0.1;

    /**
     * 是否启用特征匹配维度
     */
    private boolean featureMatchingEnabled = true;

    /**
     * 特征匹配权重
     */
    private double featureMatchingWeight = 0.1;
}
