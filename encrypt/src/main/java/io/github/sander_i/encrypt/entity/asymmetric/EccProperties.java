package io.github.sander_i.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;

/**
 * ECC 椭圆曲线运行时配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EccProperties extends EncryptProperties {

    /**
     * 椭圆曲线名称
     */
    private String curve;

    {
        setAlgorithm(AlgorithmEnum.EC.getAlgorithm());
        setCurve("secp256r1");
        setKeyBits(256);
    }

}
