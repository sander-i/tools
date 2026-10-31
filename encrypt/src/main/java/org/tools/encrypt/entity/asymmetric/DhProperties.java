package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;

import javax.crypto.spec.DHParameterSpec;

/**
 * DH 密钥协商配置项：算法、密钥位数、公私钥与域参数。
 *
 * <p>DH 不提供加解密能力，因此本配置不参与 {@code AsymmetricUtils} / {@code CipherAsymmetric}
 * 的加解密流程，仅用于密钥对生成与共享秘密协商。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DhProperties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.DH.getAlgorithm());
        /*
         * 密钥位数
         * 512
         * 1024
         * 2048
         * 4096（取决于 JDK Provider 支持范围）
         */
        setKeyBits(2048);
    }

    /**
     * DH 域参数（素数 p、生成元 g 与可选私钥长度 l）
     */
    private DHParameterSpec params;
}
