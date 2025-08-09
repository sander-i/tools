package org.tools.encrypt.entity.asymmetric;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.SignAlgorithmEnum;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RsaProperties extends EncryptProperties {

    /**
     * RSA公钥
     */
    private String publicKeyStr;

    /**
     * RSA公钥对象
     */
    private PublicKey publicKey;

    /**
     * RSA私钥
     */
    private String privateKeyStr;

    /**
     * RSA私钥对象
     */
    private PrivateKey privateKey;

    {
        setAlgorithm(AlgorithmEnum.RSA.getAlgorithm());
        /*
         * 算法模式
         * RSA
         * RSA/None/NoPadding
         * ======
         * RSA/None/OAEPWITHSHA-256ANDMGF1PADDING
         * RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
         */
        setAlgorithmMode(AlgorithmModeEnum.RSA_ECB_OAEPWITHSHA256ANDMGF1PADDING.getAlgorithmMode());
        setSignAlgorithm(SignAlgorithmEnum.MD5_WITH_RSA.getSignAlgorithm());
        /*
         * 密钥位数
         * 512
         * 1024
         * 2048
         * 4096
         */
        setKeyBits(1024);
        // RSA最大加密明文大小
        setMaxEncryptBlock(117);
        // RSA最大解密密文大小
        setMaxDecryptBlock(128);
    }

}
