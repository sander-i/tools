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

    /** 计算指定算法的消息摘要（JCA MessageDigest） */
    public static byte[] digest(String algorithm, byte[] raw) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm).digest(raw);
    }

}
