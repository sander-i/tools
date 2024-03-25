package org.tools.encrypt.symmetrical.des.impl;

import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.symmetrical.des.DesKeyGenerator;
import org.tools.encrypt.symmetrical.des.abst.DesAbst;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:19
 * @description des加密实现
 * @scope all
 */
@Deprecated
public class DesImpl extends DesAbst {

    @Override
    public String encode(String raw) throws DesException {
        return encode(getDesProperties().getDesKey(), raw);
    }

    @Override
    public String decode(String wrought) throws DesException {
        return decode(getDesProperties().getDesKey(), wrought);
    }

    @Override
    public String sign(String wrought) throws DesException {
        return null;
    }

    @Override
    public Boolean verify(String wrought, String sign) throws DesException {
        return false;
    }

    @Override
    public String encode(String secretKey, String raw) throws DesException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DesException(e);
        }
        return encode(key, raw);
    }

    @Override
    public String encode(SecretKey secretKey, String raw) throws DesException {
        // 加密前处理
        byte[] before = encryptBefore(raw);
        // 执行加密
        byte[] execute;
        try {
            execute = actuator(before, secretKey, Cipher.ENCRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new DesException(e);
        }
        // 加密后处理
        return encryptAfter(execute);
    }

    @Override
    public String decode(String secretKey, String wrought) throws DesException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DesException(e);
        }
        return decode(key, wrought);
    }

    @Override
    public String decode(SecretKey secretKey, String wrought) throws DesException {
        // 解密前处理
        byte[] before = decryptBefore(wrought);
        // 执行解密
        byte[] execute;
        try {
            execute = actuator(before, secretKey, Cipher.DECRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new DesException(e);
        }
        // 解密后处理
        return decryptAfter(execute);
    }

}
