package org.tools.encrypt.basic.md5;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.exception.EncryptException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/26 21:59
 * @description md5工具
 * @scope all
 */
@Slf4j
public class MD5Tool {

    private MD5Tool() {
    }

    /**
     * md5配置
     */
    @Getter
    @Setter
    private static MD5Properties properties = MD5Properties.newInstance();

    /**
     * 字节类型数组MD5加密
     *
     * @param raw 待加密
     * @return byte[]
     */
    private static byte[] byteMD5(byte[] raw) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(properties.getAlgorithm());
        return md5.digest(raw);
    }

    /**
     * 将字节数组换成16进制的字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    private static String byteToHex(byte[] byteArray) {
        //16是表示转换为16进制数
        return new BigInteger(properties.getSignum(), byteArray).toString(properties.getRadix());
    }

    /**
     * md5小写加密
     *
     * @param raw 待加密
     * @return String
     */
    private static String stringMD5(String raw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] digest = byteMD5(raw.getBytes(properties.getCharsetName()));
        return byteToHex(digest);
    }

    /**
     * 将字节数组换成16进制的字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    private static String byteArrayToHex(byte[] byteArray) {
        // 初始化字符数组存放16进制字符
        char[] hexDigits = properties.getHexDigits();
        // 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    /**
     * @param raw 原字符串
     * @return String
     * @throws EncryptException 加密异常
     */
    public static String encode(String raw) throws EncryptException {
        if (BaseUtils.isEmpty(raw)) {
            return "";
        }
        byte[] resultByteArray;
        // 转字节数组 加密
        try {
            resultByteArray = byteMD5(raw.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        // 转字符串
        return byteArrayToHex(resultByteArray);
    }

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return String
     * @throws EncryptException 加密异常
     */
    public static String encode(String raw, boolean isLower) throws EncryptException {
        if (BaseUtils.isEmpty(raw)) {
            return "";
        }
        try {
            return isLower ? stringMD5(raw) : encode(raw);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new EncryptException(e);
        }
    }

    /**
     * @param raw 原字符串
     * @return String
     * @throws EncryptException 加密异常
     */
    public static String encode(byte[] raw) throws EncryptException {
        if (BaseUtils.isEmpty(raw)) {
            return "";
        }
        byte[] bytes;
        try {
            bytes = byteMD5(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        return byteArrayToHex(bytes);
    }

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return String
     * @throws EncryptException 加密异常
     */
    public static String encode(byte[] raw, boolean isLower) throws EncryptException {
        try {
            return isLower ? byteToHex(byteMD5(raw)) : encode(raw);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
    }

}
