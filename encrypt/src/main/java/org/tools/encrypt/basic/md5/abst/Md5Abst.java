package org.tools.encrypt.basic.md5.abst;

import org.tools.encrypt.basic.md5.Md5;
import org.tools.encrypt.basic.md5.Md5Properties;
import org.tools.encrypt.exception.basic.BasicException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 15:17
 * @description md5 加密抽象类
 * @scope md5
 */
public abstract class Md5Abst implements Md5 {

    private Md5Properties properties;

    public Md5Properties getMd5Properties() {
        return properties;
    }

    public void setMd5Properties(Md5Properties properties) {
        this.properties = properties;
    }

    /**
     * md5小写加密
     *
     * @param raw 待加密
     * @return byte[]
     */
    public byte[] byteMd5(byte[] raw) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(Md5Properties.ALGORITHM);
        return md5.digest(raw);
    }

    /**
     * 将字节数组换成16进制的字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    public String byteToHex(byte[] byteArray) {
        //16是表示转换为16进制数
        return new BigInteger(Md5Properties.SIGNUM, byteArray).toString(Md5Properties.RADIX);
    }

    /**
     * md5小写加密
     *
     * @param raw 待加密
     * @return String
     */
    public String stringMd5(String raw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] digest = byteMd5(raw.getBytes(properties.getCharsetName()));
        return byteToHex(digest);
    }

    /**
     * 字节类型数组MD5加密
     *
     * @param raw 待加密
     * @return byte[]
     */
    public byte[] byteMD5(byte[] raw) throws NoSuchAlgorithmException {
        // 拿到一个MD5转换器
        MessageDigest messageDigest = MessageDigest.getInstance(Md5Properties.ALGORITHM);
        // 字节数组，包含16个元素
        return messageDigest.digest(raw);
    }

    /**
     * 将字节数组换成16进制的字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    public String byteArrayToHex(byte[] byteArray) {
        // 初始化字符数组存放16进制字符
        char[] hexDigits = Md5Properties.HEX_DIGITS;
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
     * 字符串类型MD5加密
     *
     * @param raw 待加密
     * @return String
     */
    public String stringMD5(String raw) throws BasicException {
        byte[] resultByteArray;
        // 转字节数组 加密
        try {
            resultByteArray = byteMD5(raw.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new BasicException(e);
        }
        // 转字符串
        return byteArrayToHex(resultByteArray);
    }

}
