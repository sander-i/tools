package org.tools.encrypt.symmetrical.des.abst;

import org.tools.encrypt.symmetrical.des.Des;
import org.tools.encrypt.symmetrical.des.DesProperties;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:16
 * @description des加密抽象类
 * @scope des
 */
public abstract class DesAbst implements Des {

    /**
     * 配置文件
     */
    protected DesProperties desProperties;

    @Override
    public DesProperties getDesProperties() {
        return desProperties;
    }

    @Override
    public Des setDesProperties(DesProperties desProperties) {
        this.desProperties = desProperties;
        return this;
    }

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
        Cipher cipher = Cipher.getInstance(desProperties.getAlgorithmMode());
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

}
