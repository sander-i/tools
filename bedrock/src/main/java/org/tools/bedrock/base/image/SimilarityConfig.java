package org.tools.bedrock.base.image;

import lombok.Data;

/**
 * 图像相似度融合计算方法配置类
 *
 * @author sander
 * @version 1.0
 */
@Data
public class SimilarityConfig {
    private boolean pixelMatchingEnabled = true;
    private double pixelMatchingWeight = 0.15;
    private boolean histogramEnabled = true;
    private double histogramWeight = 0.15;
    private boolean ssimEnabled = true;
    private double ssimWeight = 0.25;
    private boolean avgHashEnabled = true;
    private double avgHashWeight = 0.1;
    private boolean pHashEnabled = true;
    private double pHashWeight = 0.15;
    private boolean dHashEnabled = true;
    private double dHashWeight = 0.1;
    private boolean featureMatchingEnabled = true;
    private double featureMatchingWeight = 0.1;

}