package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * 3DES 加密配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.des3")
public class Des3 extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * DESede
     * DESede/CBC/PKCS5Padding
     * DESede/ECB/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.DES_EDE_CBC_PKCS5PADDING.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.DES_EDE.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 168;

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
