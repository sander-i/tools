package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import io.github.sander_i.encrypt.entity.KeyPair;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * RC4 加密配置（流加密，无分组模式与向量）
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "tools.encrypt.symmetrical.rc4")
public class Rc4 extends KeyPair {

    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式，RC4 为流加密，只有 RC4 一种
     */
    private String algorithmMode = AlgorithmModeEnum.RC4.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.RC4.getAlgorithm();

    /**
     * 密钥位数，RC4 支持 40~1024 位，默认 128
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
