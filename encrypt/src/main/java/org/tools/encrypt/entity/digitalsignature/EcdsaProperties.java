package org.tools.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.SignAlgorithmEnum;

/**
 * ECDSA 配置项：公私钥及其字符串形式、椭圆曲线名称。
 *
 * @author sander
 * @version 1.0
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EcdsaProperties extends EncryptProperties {
    /**
     * 椭圆曲线名称（默认 secp256r1）
     */
    private String curve;

    {
        setAlgorithm(AlgorithmEnum.EC.getAlgorithm());
        setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_ECDSA.getSignAlgorithm());
        /*
         * 密钥位数
         * 256
         * 384
         * 521
         */
        setKeyBits(256);
        setCurve("secp256r1");
    }
}
