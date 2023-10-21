package org.tools.encrypt.basic.base.impl;

import org.tools.encrypt.basic.base.abst.BaseAbst;
import org.tools.encrypt.exception.basic.BasicException;

import java.nio.charset.StandardCharsets;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/25 11:29
 * @description base64加密实现
 * @scope base
 */
public class BaseImpl extends BaseAbst {

    @Override
    public String encode(String raw) throws BasicException {
        byte[] bytes = raw.getBytes();
        return encode(bytes);
    }

    @Override
    public String encode(byte[] raw) throws BasicException {
        return encoder.encodeToString(raw);
    }

    @Override
    public String decode(String wrought) throws BasicException {
        return new String(decoder.decode(wrought), StandardCharsets.UTF_8);
    }

    @Override
    public String sign(String wrought) throws BasicException {
        return null;
    }

    @Override
    public Boolean verify(String wrought, String sign) throws BasicException {
        return false;
    }

}
