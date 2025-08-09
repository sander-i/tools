package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;
import org.tools.encrypt.enums.SignAlgorithmEnum;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RsaProperties extends EncryptProperties {

    {
        setAlgorithm(AlgorithmEnum.RSA.getAlgorithm());
        /*
         * 算法模式
         * RSA
         * RSA/ECB/NoPadding
         * ======
         * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
         * OAEPWithSHA-256AndMGF1Padding
         */
        setAlgorithmMode(AlgorithmModeEnum.RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING.getAlgorithmMode());
        setSignAlgorithm(SignAlgorithmEnum.SHA3_256_WITH_RSA.getSignAlgorithm());
        /*
         * 密钥位数
         * 512
         * 1024
         * 2048
         * 4096
         */
        setKeyBits(2048);
        switch (AlgorithmModeEnum.enumName(algorithmMode)) {
            case RSA_ECB_NO_PADDING: {
                setMaxEncryptBlock((keyBits / 8) - 11);
                setMaxDecryptBlock(keyBits / 8);
                break;
            }
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
