package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.util.List;

/**
 * 3DES（DESede）密钥对配置实体。
 *
 * <p>默认算法 {@code DESede}、默认模式 {@code DESede/CBC/PKCS5Padding}、默认密钥位数 168。
 * 密钥为 Hex 字符串（16 字节 / 32 Hex 或 24 字节 / 48 Hex）。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Des3 extends KeyPair {
    /**
     * 是否启用
     */
    private Boolean enabled = false;

    /**
     * 算法模式
     * DESede/CBC/PKCS5Padding
     */
    private String algorithmMode = AlgorithmModeEnum.DES_EDE_CBC_PKCS5Padding.getAlgorithmMode();

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.DES_EDE.getAlgorithm();

    /**
     * 密钥位数
     */
    private int keyBits = 168;

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
