package org.tools.encrypt.basic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/26 21:49
 * @description 基础工具
 * @scope all
 */
public class BasicTool {

    private BasicTool() {
    }

    public static byte[] digest(String algorithm, byte[] raw) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm).digest(raw);
    }

}
