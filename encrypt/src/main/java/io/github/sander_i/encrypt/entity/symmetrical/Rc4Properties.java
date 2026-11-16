package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

/**
 * RC4 加密运行时配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc4Properties extends EncryptProperties {

    {
        setAlgorithmMode(AlgorithmModeEnum.RC4.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.RC4.getAlgorithm());
        setKeyBits(128);
    }

}
