package org.tools.encrypt.digitalsignature.dsa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.SignAlgorithmEnum;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
@Data
public class DsaProperties {

    /***
     * 算法
     */
    public String algorithm = AlgorithmEnum.DSA.getAlgorithm();
    /**
     * 验签算法
     */
    public String signAlgorithm = SignAlgorithmEnum.SHA256_WITH_DSA.getSignAlgorithm();
    /**
     * 密钥位数
     * 512
     * 1024
     * 2048
     * 4096
     */
    private int keyBits = 1024;
    /**
     * RSA公钥
     */
    private String publicKey;
    /**
     * RSA公钥对象
     */
    private PublicKey dsaPublicKey;
    /**
     * RSA私钥
     */
    private String privateKey;
    /**
     * RSA私钥对象
     */
    private PrivateKey dsaPrivateKey;

}
