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

    public static byte[] digest(String algorithm, byte[] raw) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm).digest(raw);
    }

}
