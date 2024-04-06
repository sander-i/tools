package org.tools.encrypt.entity;

import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/03/15
 */
@Data
public class KeyPair {

    private String name;

    /**
     * 算法模式
     */
    private String algorithmMode;

    /***
     * 算法
     */
    public String algorithm;

    /**
     * 验签算法
     */
    public String signAlgorithm;

    /**
     * 密钥位数
     * 512
     * 1024
     * 2048
     * 4096
     */
    private int keyBits;

    /**
     * 最大加密明文大小
     */
    private Integer maxEncryptBlock = 117;

    /**
     * 最大解密密文大小
     */
    private Integer maxDecryptBlock = 128;

    /**
     * 向量
     */
    private String ivParameter;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 密钥
     */
    private String secretKey;

}
