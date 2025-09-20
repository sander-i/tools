package org.tools.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.SignAlgorithmEnum;

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
