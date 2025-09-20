package org.tools.encrypt.entity.symmetrical;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * @author sander
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.idea")
public class Idea extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * IDEA/ECB/ISO10126Padding
     */
    private String algorithmMode = AlgorithmModeEnum.IDEA_ECB_ISO10126Padding.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.IDEA.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 128;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 分组是否启用
     */
    private Boolean groupEnabled = false;

    /**
     * 分组
     */
    private List<KeyPair> group;

}
