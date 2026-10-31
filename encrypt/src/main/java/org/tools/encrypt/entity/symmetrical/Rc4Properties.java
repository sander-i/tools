package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * RC4 加密配置文件：默认 {@code RC4}、128 位密钥。
 *
 * <p>RC4 为流密码，无 IV、无填充、无分组，{@code ivParameter} 不参与加解密运算。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
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
