package org.tools.encrypt;

import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/01 23:20
 * @description 加密接口
 * @scope all
 */
@Deprecated
public interface Encrypt {

    /**
     * 加密
     *
     * @param raw 原字符串
     * @return String
     */
    String encode(String raw) throws EncryptException;

    /**
     * 解密
     *
     * @param wrought 处理后字符串
     * @return String
     */
    String decode(String wrought) throws EncryptException;

    /**
     * 签名
     *
     * @param wrought 加密数据
     * @return 数字签名
     */
    String sign(String wrought) throws EncryptException;

    /**
     * 验签
     *
     * @param wrought 加密数据
     * @param sign    数字签名
     * @return 验签结果
     */
    Boolean verify(String wrought, String sign) throws EncryptException;

}
