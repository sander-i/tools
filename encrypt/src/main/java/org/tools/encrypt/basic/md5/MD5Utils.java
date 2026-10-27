package org.tools.encrypt.basic.md5;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.basic.BasicUtils;
import org.tools.encrypt.exception.EncryptException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 摘要工具。
 *
 * <p>输出默认为大写十六进制；{@link #encode(String)} 固定按 UTF-8 取字节
 * （此前使用 JVM 默认 charset，跨平台结果不一致，已修复）。
 * {@link #encode(String, boolean)} 的小写路径使用 BigInteger 转换，可能丢失前导零。
 * MD5 不具备抗碰撞性，仅用于数据指纹等非安全场景。</p>
 *
 * <p>线程安全：每次 encode 新建 MessageDigest，可并发使用。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.1
 */
@Slf4j
public class MD5Utils {
    /**
     * md5配置
     */
    @Getter
    @Setter
    private static MD5Properties properties;

    private MD5Utils() {
    }

    private static void init() {
        if (BaseUtils.isEmpty(properties)) {
            properties = new MD5Properties();
        }
    }

    /**
     * 装载 MD5 配置（properties 为 null 时抛 EncryptException）
     *
     * @param properties MD5 配置
     * @throws EncryptException 配置为空时抛出
     */
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
     * @param raw 原字符串，固定按 UTF-8 取字节
     * @return 密文
     * @throws EncryptException 加密异常
     */
    public static String encode(String raw) throws EncryptException {
        return encode(raw.getBytes(StandardCharsets.UTF_8), false);
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
        return encode(raw.getBytes(StandardCharsets.UTF_8), isLower);
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
