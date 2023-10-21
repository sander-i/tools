package org.tools.encrypt.basic.base;

import org.tools.encrypt.Encrypt;
import org.tools.encrypt.exception.basic.BasicException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/24 23:43
 * @description 8Bit字节码的编码方式
 * @scope base
 */
public interface Base extends Encrypt {

    /**
     * 加密
     *
     * @param raw 原字符串
     * @return String
     * @throws BasicException 基础加密异常
     */
    String encode(byte[] raw) throws BasicException;

}
