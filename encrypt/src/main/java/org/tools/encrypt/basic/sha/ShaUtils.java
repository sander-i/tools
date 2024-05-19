package org.tools.encrypt.basic.sha;

import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.basic.BasicUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/28
 * @description sha加密
 * @scope sha
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

    public static String encode(String raw) throws NoSuchAlgorithmException {
        return encode(AlgorithmEnum.SHA256.getAlgorithm(), raw);
    }

    public static String encode(String algorithm, String raw) throws NoSuchAlgorithmException {
        // 计算哈希值
        byte[] digest = BasicUtils.digest(algorithm, raw.getBytes());
        return byteToHex(digest);
    }

}
