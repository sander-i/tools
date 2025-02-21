package org.tools.encrypt.entity.digitalsignature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.SignAlgorithmEnum;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DsaProperties extends EncryptProperties {

    /**
     * DSA公钥
     */
    private String publicKeyStr;

    /**
     * DSA公钥对象
     */
    private PublicKey publicKey;

    /**
     * DSA私钥
     */
    private String privateKeyStr;

    /**
     * DSA私钥对象
     */
    private PrivateKey privateKey;

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
