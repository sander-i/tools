package org.tools.encrypt.basic.md5;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.basic.BasicUtils;
import org.tools.encrypt.exception.EncryptException;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class MD5Utils {

    private MD5Utils() {
    }

    /**
     * md5配置
     */
    @Getter
    @Setter
    private static MD5Properties properties;

    private static void init() {
        if (BaseUtils.isEmpty(properties)) {
            properties = new MD5Properties();
        }
    }

    public static void load(MD5Properties properties) throws EncryptException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EncryptException("properties 不能为空！");
        }
        MD5Utils.properties = properties;
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
     * @return 密文
     * @throws EncryptException 加密异常
     */
    public static String encode(String raw) throws EncryptException {
        return encode(raw.getBytes(), false);
    }

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return 密文
     * @throws EncryptException 加密异常
     */
    public static String encode(String raw, boolean isLower) throws EncryptException {
        if (BaseUtils.isEmpty(raw)) {
            return "";
        }
        return encode(raw.getBytes(), isLower);
    }

    /**
     * @param raw 原字符串
     * @return 密文
     * @throws EncryptException 加密异常
     */
    public static String encode(byte[] raw) throws EncryptException {
        return encode(raw, false);
    }

    /**
     * @param raw     原字符串
     * @param isLower 小写加密
     * @return 密文
     * @throws EncryptException 加密异常
     */
    public static String encode(byte[] raw, boolean isLower) throws EncryptException {
        if (BaseUtils.isEmpty(raw)) {
            return "";
        }
        if (BaseUtils.isEmpty(properties)) {
            init();
        }
        byte[] bytes;
        try {
            bytes = BasicUtils.digest(properties.getAlgorithm(), raw);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        return isLower ? byteToHex(bytes) : byteArrayToHex(bytes);
    }

}
