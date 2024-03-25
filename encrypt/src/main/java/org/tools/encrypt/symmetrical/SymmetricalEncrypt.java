package org.tools.encrypt.symmetrical;

import org.tools.encrypt.Encrypt;
import org.tools.encrypt.exception.symmetrical.SymmetricalException;

import javax.crypto.SecretKey;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/01 23:24
 * @description 对称加密接口
 * @scope symmetrical
 */
@Deprecated
public interface SymmetricalEncrypt extends Encrypt {

    /**
     * 密钥加密
     *
     * @param secretKey 密钥
     * @param raw       原字符串
     * @return 密文
     * @throws SymmetricalException 对称加密异常
     */
    String encode(String secretKey, String raw) throws SymmetricalException;

    /**
     * 密钥加密
     *
     * @param secretKey 密钥
     * @param raw       原字符串
     * @return 密文
     * @throws SymmetricalException 对称加密异常
     */
    String encode(SecretKey secretKey, String raw) throws SymmetricalException;

    /**
     * 密钥解密
     *
     * @param secretKey 密钥
     * @param wrought   加密数据
     * @return 原文
     * @throws SymmetricalException 对称加密异常
     */
    String decode(String secretKey, String wrought) throws SymmetricalException;

    /**
     * 密钥解密
     *
     * @param secretKey 密钥
     * @param wrought   加密数据
     * @return 原文
     * @throws SymmetricalException 对称加密异常
     */
    String decode(SecretKey secretKey, String wrought) throws SymmetricalException;

}
