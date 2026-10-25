package org.tools.encrypt.basic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 */
public class BasicUtils {

    private BasicUtils() {
    }

    /**
     * 计算指定算法的消息摘要（JCA MessageDigest）
     *
     * @param algorithm 摘要算法名（如 MD5、SHA-256）
     * @param raw       待摘要的字节数据
     * @return 摘要结果字节数组
     * @throws NoSuchAlgorithmException 算法不可用时抛出
     */
    public static byte[] digest(String algorithm, byte[] raw) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm).digest(raw);
    }

}
