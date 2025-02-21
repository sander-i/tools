package org.tools.encrypt.entity;


import lombok.Data;
import org.tools.bedrock.annotations.Inherit;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 **/
@Inherit
@Data
public class EncryptProperties {

    /**
     * 算法
     */
    protected String algorithm;

    /**
     * 算法模式
     */
    private String algorithmMode;

    /**
     * 验签算法
     */
    protected String signAlgorithm;

    /**
     * 密钥位数
     */
    protected int keyBits;

    /**
     * 最大加密明文大小
     */
    private Integer maxEncryptBlock;

    /**
     * 最大解密密文大小
     */
    private Integer maxDecryptBlock;

    /**
     * 向量
     */
    protected String ivParameter;

    /**
     * 密钥
     */
    private String secretKeyStr;

    /**
     * 密钥对象
     */
    private SecretKey secretKey;

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

}
