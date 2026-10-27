package org.tools.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.SignAlgorithmEnum;

/**
 * DSA 配置项：公私钥及其字符串形式。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DsaProperties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.DSA.getAlgorithm());
        // 默认签名算法此前被误设到 algorithmMode 字段，signAlgorithm 为 null，
        // 导致 DsaHelper.load 时 Signature.getInstance(null) 抛 NPE，已修正
        setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm());
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
