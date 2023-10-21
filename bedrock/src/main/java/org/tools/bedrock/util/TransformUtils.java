package org.tools.bedrock.util;

import lombok.extern.log4j.Log4j2;
import org.tools.bedrock.constant.Constants;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:22
 * @description 转换工具
 * @scope all
 */
@Log4j2
public class TransformUtils {

    private TransformUtils() {
    }

    /**
     * 字符串转字符数组
     *
     * @param str 字符串
     * @return 字符数组
     */
    public static char[] strToChar(String str) {
        return str.toCharArray();
    }

    /**
     * 字符串转字符数组
     *
     * @param chars 字符数组
     * @return 字符串
     */
    public static String charToStr(char[] chars) {
        return new String(chars);
    }

    /**
     * 字符串转字节数组
     *
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] strToByte(String str) {
        return str.getBytes();
    }

    /**
     * 字符串转字节数组
     *
     * @param str     字符串
     * @param charset 字符集
     * @return 字节数组
     */
    public static byte[] strToByte(String str, String charset) {
        try {
            return str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            log.error("字符串转字节数组", e);
        }
        return Constants.EMPTY_BYTE_ARRAY;
    }

    /**
     * 字符串转字节数组
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String byteToStr(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * 字符串转字节数组
     *
     * @param bytes   字节数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String byteToStr(byte[] bytes, String charset) {
        return new String(bytes, Charset.forName(charset));
    }

}
