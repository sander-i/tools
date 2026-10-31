package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;

/**
 * ECC 椭圆曲线配置项：算法、密钥位数与曲线名。
 *
 * <p>默认曲线 {@code secp256r1}（P-256），仅用于 ECDH 密钥协商；
 * 本模块不提供 ECIES 加解密。</p>
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EccProperties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.EC.getAlgorithm());
        /*
         * 密钥位数
         * 256
         * 384
         * 521
         */
        setKeyBits(256);
    }

    /**
     * 曲线名
     */
    private String curve = "secp256r1";
}
