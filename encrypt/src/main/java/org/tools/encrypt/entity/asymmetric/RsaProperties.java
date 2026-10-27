package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.enums.SignAlgorithmEnum;

/**
 * RsaProperties。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RsaProperties extends EncryptProperties {
    {
        setAlgorithm(AlgorithmEnum.RSA.getAlgorithm());
        /*
         * 算法模式
         * RSA
         * ======
         * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
         * OAEPWithSHA-256AndMGF1Padding
         */
        setAlgorithmMode(AlgorithmModeEnum.RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING.getAlgorithmMode());
        // 默认 SHA256withRSA：JDK 8 默认 Provider（SunRsaSign）不支持 SHA3-256withRSA，
        // 旧默认值在纯 JDK 8 环境装载即抛 NoSuchAlgorithmException
        setSignAlgorithm(SignAlgorithmEnum.SHA256_WITH_RSA.getSignAlgorithm());
        /*
         * 密钥位数
         * 512
         * 1024
         * 2048
         * 4096
         */
        setKeyBits(2048);
        recalcMaxBlocks();
    }

    /**
     * 按当前 algorithmMode 与 keyBits 重算最大加解密块大小。
     *
     * <p>修改 {@code algorithmMode} 或 {@code keyBits} 后需要调用本方法同步块大小，
     * 否则分段加解密边界与密钥规格不匹配。</p>
     */
    public void recalcMaxBlocks() {
        switch (AlgorithmModeEnum.enumName(algorithmMode)) {
            case RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING: {
                int hLen = 256 / 8;
                setMaxEncryptBlock((keyBits / 8) - (2 * hLen) - 2);
                setMaxDecryptBlock(keyBits / 8);
                break;
            }
            case OAEPWithSHA__256AndMGF1Padding: {
                setMaxEncryptBlock((keyBits / 8) - 66);
                setMaxDecryptBlock(keyBits / 8);
                break;
            }
            default:
                setMaxEncryptBlock(117);
                setMaxDecryptBlock(128);
                break;
        }
    }
}
