package org.tools.encrypt.digitalsignature;

import org.tools.encrypt.Encrypt;
import org.tools.encrypt.exception.digitalsignature.DigitalsignatureException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:12
 * @description 签名算法接口
 * @scope digitalsignature
 */
@Deprecated
public interface DigitalsignatureEncrypt extends Encrypt {

    /**
     * 密钥签名
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 数字签名
     * @throws DigitalsignatureException 签名算法异常
     */
    String sign(String privateKey, String wrought) throws DigitalsignatureException;

    /**
     * 密钥签名
     *
     * @param privateKey 私钥
     * @param wrought    加密数据
     * @return 数字签名
     * @throws DigitalsignatureException 签名算法异常
     */
    String sign(PrivateKey privateKey, String wrought) throws DigitalsignatureException;

    /**
     * 密钥验签
     *
     * @param publicKey 公钥
     * @param wrought   加密数据
     * @param sign      数字签名
     * @return 验签结果
     * @throws DigitalsignatureException 签名算法异常
     */
    Boolean verify(String publicKey, String wrought, String sign) throws DigitalsignatureException;

    /**
     * 密钥验签
     *
     * @param publicKey 公钥
     * @param wrought   加密数据
     * @param sign      数字签名
     * @return 验签结果
     * @throws DigitalsignatureException 签名算法异常
     */
    Boolean verify(PublicKey publicKey, String wrought, String sign) throws DigitalsignatureException;

}
