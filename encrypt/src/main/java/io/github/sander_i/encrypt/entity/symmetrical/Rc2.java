package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * RC2 加密配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.rc2")
public class Rc2 extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * RC2
     * RC2/ECB/PKCS5Padding
     * RC2/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.RC2_CBC_PKCS5PADDING.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.RC2.getAlgorithm();

    /**
     * 密钥位数，RC2 支持 40~1024 位，默认 128
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
