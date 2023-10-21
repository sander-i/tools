package org.tools.encrypt.basic.sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 18:24
 * @description md5加密
 * @scope md5
 */
public class Md5 {

    public static final String ALGORITHM = "MD5";
    public static final int SIGNUM = 1;
    public static final int RADIX = 16;
    public static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String charsetName = "utf-8";

    public static String encode() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);



        return "";
    }

}
