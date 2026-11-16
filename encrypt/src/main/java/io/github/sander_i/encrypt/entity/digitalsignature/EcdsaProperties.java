package io.github.sander_i.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.SignAlgorithmEnum;

/**
 * ECDSA 签名配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EcdsaProperties extends EncryptProperties {

    {
        setAlgorithm(AlgorithmEnum.EC.getAlgorithm());
        setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_ECDSA.getSignAlgorithm());
        setKeyBits(256);
    }

}
