package org.tools.encrypt.basic.md5;

import org.tools.encrypt.Encrypt;
import org.tools.encrypt.exception.basic.BasicException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 15:04
 * @description md5 加密
 * @scope md5
 */
public interface Md5 extends Encrypt {

    Md5Properties getMd5Properties();

    void setMd5Properties(Md5Properties md5Properties);

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return String
     * @throws BasicException 基础加密异常
     */
    String encode(String raw, boolean isLower) throws BasicException;

    /**
     * @param raw 原字符串
     * @return String
     */
    String encode(byte[] raw) throws BasicException;

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return String
     * @throws BasicException 基础加密异常
     */
    String encode(byte[] raw, boolean isLower) throws BasicException;

}
