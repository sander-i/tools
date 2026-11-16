package io.github.sander_i.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.SignAlgorithmEnum;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DsaProperties extends EncryptProperties {

    {
        setAlgorithm(AlgorithmEnum.DSA.getAlgorithm());
        setAlgorithmMode(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
        /*
         * 密钥位数
         * 512
         * 1024
         * 2048
         * 4096
         */
        setKeyBits(1024);
    }

}
