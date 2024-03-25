package org.tools.encrypt.basic.md5.impl;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.basic.md5.abst.Md5Abst;
import org.tools.encrypt.exception.basic.BasicException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 15:31
 * @description md5工具
 * @scope md5
 */
@Deprecated
public class Md5Impl extends Md5Abst {

    @Override
    public String encode(String raw) throws BasicException {
        if (BaseUtils.isEmpty(raw)) {
            return null;
        }
        return stringMD5(raw);
    }

    @Override
    public String decode(String wrought) throws BasicException {
        return null;
    }

    @Override
    public String sign(String wrought) throws BasicException {
        return null;
    }

    @Override
    public Boolean verify(String wrought, String sign) throws BasicException {
        return false;
    }

    @Override
    public String encode(String raw, boolean isLower) throws BasicException {
        if (BaseUtils.isEmpty(raw)) {
            return null;
        }
        try {
            return isLower ? stringMd5(raw) : encode(raw);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new BasicException(e);
        }
    }

    @Override
    public String encode(byte[] raw) throws BasicException {
        if (BaseUtils.isEmpty(raw)) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = byteMD5(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new BasicException(e);
        }
        return byteArrayToHex(bytes);
    }

    @Override
    public String encode(byte[] raw, boolean isLower) throws BasicException {
        try {
            return isLower ? byteToHex(byteMd5(raw)) : encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new BasicException(e);
        }
    }

}
