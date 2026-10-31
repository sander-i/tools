package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * RC2 对称加密配置实体。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc2 extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * RC2
     * RC2/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.RC2.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.RC2.getAlgorithm();

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
