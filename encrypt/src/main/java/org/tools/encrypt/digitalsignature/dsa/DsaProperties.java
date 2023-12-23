package org.tools.encrypt.digitalsignature.dsa;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:18
 * @description dsa签名配置文件
 * @scope dsa
 */
@Slf4j
public class DsaProperties {

    /***
     * 算法
     */
    public static final String ALGORITHM = "DSA";
    /**
     * 验签算法
     */
    public static final String SIGN_ALGORITHM = "SHA256withDSA";
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

    public int getKeyBits() {
        return keyBits;
    }

    public void setKeyBits(int keyBits) {
        this.keyBits = keyBits;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public PublicKey getDsaPublicKey() {
        return dsaPublicKey;
    }

    public void setDsaPublicKey(PublicKey dsaPublicKey) {
        this.dsaPublicKey = dsaPublicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public PrivateKey getDsaPrivateKey() {
        return dsaPrivateKey;
    }

    public void setDsaPrivateKey(PrivateKey dsaPrivateKey) {
        this.dsaPrivateKey = dsaPrivateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        this.setDsaPublicKey();
    }

    public void setDsaPublicKey() {
        try {
            this.dsaPublicKey = DsaKeyGenerator.getPublicKey(this.publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        this.setDsaPrivateKey();
    }

    public void setDsaPrivateKey() {
        try {
            this.dsaPrivateKey = DsaKeyGenerator.getPrivateKey(this.privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
