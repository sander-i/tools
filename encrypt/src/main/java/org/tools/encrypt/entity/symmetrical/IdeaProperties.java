package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * IDEA 配置项：算法、密钥与 IV。
 *
 * @author sander
 * @since 1.0.6.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdeaProperties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.IDEA.getAlgorithm());
        /*
        IDEA/ECB/ISO10126Padding
         */
        setAlgorithmMode(AlgorithmModeEnum.IDEA_ECB_ISO10126Padding.getAlgorithmMode());
        setKeyBits(128);
    }
}
