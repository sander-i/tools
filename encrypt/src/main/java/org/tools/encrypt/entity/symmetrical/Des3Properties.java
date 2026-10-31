package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

/**
 * 3DES（DESede）加密配置文件。
 *
 * <p>默认算法为 {@code DESede}，默认模式为 {@code DESede/CBC/PKCS5Padding}，
 * 默认密钥位数 168；密钥为 Hex 字符串（16 字节 / 32 Hex 或 24 字节 / 48 Hex），
 * CBC 模式下 IV 按 UTF-8 取字节后必须为 8 字节。</p>
 *
 * @author sander
 * @since 2.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Des3Properties extends EncryptProperties {
    {
        setAlgorithmMode(AlgorithmModeEnum.DES_EDE_CBC_PKCS5Padding.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.DES_EDE.getAlgorithm());
        setKeyBits(168);
    }
}
