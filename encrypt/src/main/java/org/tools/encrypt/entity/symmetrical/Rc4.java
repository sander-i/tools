package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * RC4 对称加密配置实体。
 *
 * <p>RC4 为流密码，只有 {@code RC4} 一种模式，无 IV、无填充、无分组；
 * 密钥为 Hex 字符串，默认 128 位。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc4 extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * RC4
     */
    private String algorithmMode = AlgorithmModeEnum.RC4.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.RC4.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 128;

    /**
     * 密钥（Hex 字符串）
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
