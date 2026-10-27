package org.tools.encrypt.basic.sha;

import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.basic.BasicUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * SHA 系列摘要工具。
 *
 * <p>默认使用 SHA-256，可通过 {@link #encode(String, String)} 指定 SHA-1 / SHA-256 / SHA-384 / SHA-512。
 * 输出为小写十六进制字符串。</p>
 *
 * <p>线程安全：每次调用新建 MessageDigest，可并发使用。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.1
 */
public class ShaUtils {
    private ShaUtils() {
    }

    private static final int BINARY = 0xff;

    private static final char FILL = '0';

    private static String byteToHex(byte[] byteArray) {
        StringBuilder hexStr = new StringBuilder();
        // 转16进制
        for (byte b : byteArray) {
            String hex = Integer.toHexString(BINARY & b);
            if (hex.length() == 1) {
                hexStr.append(FILL);
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
    }

    /**
     * 计算摘要，算法为 SHA-256。
     *
     * @param raw 原文，固定按 UTF-8 取字节；为 {@code null} 时抛 NPE
     * @return 小写十六进制摘要（SHA-256 为 64 字符）
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     */
    public static String encode(String raw) throws NoSuchAlgorithmException {
        return encode(AlgorithmEnum.SHA256.getAlgorithm(), raw);
    }

    /**
     * 计算指定算法的摘要。
     *
     * @param algorithm 摘要算法名（如 SHA-1、SHA-256、SHA-384、SHA-512）
     * @param raw       原文，固定按 UTF-8 取字节；为 {@code null} 时抛 NPE
     * @return 小写十六进制摘要
     * @throws NoSuchAlgorithmException 算法名不可用时抛出
     */
    public static String encode(String algorithm, String raw) throws NoSuchAlgorithmException {
        // 计算哈希值
        byte[] digest = BasicUtils.digest(algorithm, raw.getBytes(StandardCharsets.UTF_8));
        return byteToHex(digest);
    }
}
