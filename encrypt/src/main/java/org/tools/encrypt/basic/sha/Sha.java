package org.tools.encrypt.basic.sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 17:53
 * @description sha加密
 * @scope sha
 */
public class Sha {

    private Sha() {
    }

    private static final int BINARY = 0xff;
    private static final char FILL = '0';
    /**
     * SHA-1
     * SHA-256
     * SHA-384
     * SHA-512
     */
    public static final String ALGORITHM = "SHA-256";

    public static String encode(String raw) throws NoSuchAlgorithmException {
        return encode(ALGORITHM, raw);
    }

    public static String encode(String algorithm, String raw) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 计算哈希值
        byte[] digest = messageDigest.digest(raw.getBytes());
        StringBuilder hexStr = new StringBuilder();
        // 转16进制
        for (byte b : digest) {
            String hex = Integer.toHexString(BINARY & b);
            if (hex.length() == 1) {
                hexStr.append(FILL);
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
    }

}
