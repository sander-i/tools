package org.tools.encrypt.symmetrical.des;

import lombok.Getter;
import lombok.Setter;
import org.tools.encrypt.exception.EncryptException;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/30 14:56
 * @description 描述
 * @scope 作用域
 */
public class DesUtils {

    private DesUtils() {
    }

    @Getter
    @Setter
    private DesProperties properties;

    /**
     * DES算法要求有一个可信任的随机数源
     *
     * @return SecureRandom
     */
    protected SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

    /**
     * 执行加解密操作
     *
     * @param todo 待处理
     * @param mode 模式
     * @return byte[]
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected byte[] actuator(byte[] todo, SecretKey secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(properties.getAlgorithmMode());
        // 用密匙初始化Cipher对象
        cipher.init(mode, secretKey, getSecureRandom());
        // 执行加解密操作
        return cipher.doFinal(todo);
    }

    /**
     * 加密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected byte[] encryptBefore(String todo) {
        return todo.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 加密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected String encryptAfter(byte[] todo) {
        return Base64.getEncoder().encodeToString(todo);
    }

    /**
     * 解密前处理
     *
     * @param todo 待处理
     * @return byte[]
     */
    protected byte[] decryptBefore(String todo) {
        return Base64.getDecoder().decode(todo);
    }

    /**
     * 解密后处理
     *
     * @param todo 待处理
     * @return String
     */
    protected String decryptAfter(byte[] todo) {
        return new String(todo);
    }

    public String encode(String raw) throws EncryptException {
        return encode(properties.getDesKey(), raw);
    }

    public String decode(String wrought) throws EncryptException {
        return decode(properties.getDesKey(), wrought);
    }

    public String encode(String secretKey, String raw) throws EncryptException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptException(e);
        }
        return encode(key, raw);
    }

    public String encode(SecretKey secretKey, String raw) throws EncryptException {
        // 加密前处理
        byte[] before = encryptBefore(raw);
        // 执行加密
        byte[] execute;
        try {
            execute = actuator(before, secretKey, Cipher.ENCRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new EncryptException(e);
        }
        // 加密后处理
        return encryptAfter(execute);
    }

    public String decode(String secretKey, String wrought) throws EncryptException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptException(e);
        }
        return decode(key, wrought);
    }

    public String decode(SecretKey secretKey, String wrought) throws EncryptException {
        // 解密前处理
        byte[] before = decryptBefore(wrought);
        // 执行解密
        byte[] execute;
        try {
            execute = actuator(before, secretKey, Cipher.DECRYPT_MODE);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new EncryptException(e);
        }
        // 解密后处理
        return decryptAfter(execute);
    }

}
