package org.tools.encrypt.asymmetric;

import org.tools.encrypt.Encrypt;
import org.tools.encrypt.exception.asymmetric.AsymmetricException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/01 23:47
 * @description 非对称加密接口
 * @scope asymmetric
 */
@Deprecated
public interface AsymmetricEncrypt extends Encrypt {

    /**
     * 密钥加密
     *
     * @param publicKey 公钥
     * @param raw       原字符串
     * @return 密文
     * @throws AsymmetricException 非对称加密异常
     */
    String encode(String publicKey, String raw) throws AsymmetricException;

    /**
     * 密钥加密
     *
     * @param publicKey 公钥
     * @param raw       原字符串
     * @return 密文
     * @throws AsymmetricException 非对称加密异常
     */
    String encode(PublicKey publicKey, String raw) throws AsymmetricException;

    /**
     * 密钥解密
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 原文
     * @throws AsymmetricException 非对称加密异常
     */
    String decode(String privateKey, String wrought) throws AsymmetricException;

    /**
     * 密钥解密
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 原文
     * @throws AsymmetricException 非对称加密异常
     */
    String decode(PrivateKey privateKey, String wrought) throws AsymmetricException;

    /**
     * 密钥签名
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 数字签名
     * @throws AsymmetricException 非对称加密异常
     */
    String sign(String privateKey, String wrought) throws AsymmetricException;

    /**
     * 密钥签名
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 数字签名
     * @throws AsymmetricException 非对称加密异常
     */
    String sign(PrivateKey privateKey, String wrought) throws AsymmetricException;

    /**
     * 密钥验签
     *
     * @param publicKey 公钥
     * @param wrought   加密数据
     * @param sign      数字签名
     * @return 验签结果
     * @throws AsymmetricException 非对称加密异常
     */
    Boolean verify(String publicKey, String wrought, String sign) throws AsymmetricException;

    /**
     * 密钥验签
     *
     * @param publicKey 公钥
     * @param wrought   加密数据
     * @param sign      数字签名
     * @return 验签结果
     * @throws AsymmetricException 非对称加密异常
     */
    Boolean verify(PublicKey publicKey, String wrought, String sign) throws AsymmetricException;

}
