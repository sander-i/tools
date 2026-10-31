package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * RC2 配置项：算法、密钥与 IV。
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Rc2Properties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.RC2.getAlgorithm());
        setAlgorithmMode(AlgorithmModeEnum.RC2.getAlgorithmMode());
        setKeyBits(128);
    }
}
