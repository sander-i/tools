package io.github.sander_i.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.github.sander_i.encrypt.entity.EncryptProperties;
import io.github.sander_i.encrypt.enums.AlgorithmEnum;
import io.github.sander_i.encrypt.enums.AlgorithmModeEnum;

/**
 * RC2 加密运行时配置
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc2Properties extends EncryptProperties {

    {
        setAlgorithmMode(AlgorithmModeEnum.RC2_CBC_PKCS5PADDING.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.RC2.getAlgorithm());
        setKeyBits(128);
    }

}
