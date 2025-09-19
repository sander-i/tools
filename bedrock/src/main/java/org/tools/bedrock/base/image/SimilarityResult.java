package org.tools.bedrock.base.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 图像相似度融合计算结果类
 *
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimilarityResult {
    private Map<String, Double> individualSimilarities;
    private double overallSimilarity;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimilarityResult {\n");
        sb.append("  Overall Similarity: ").append(String.format("%.4f", overallSimilarity)).append("\n");
        sb.append("  Individual Similarities:\n");
        for (Map.Entry<String, Double> entry : individualSimilarities.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(": ").append(String.format("%.4f", entry.getValue())).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}