package org.tools.encrypt.basic.md5;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 15:09
 * @description Md5加密属性
 * @scope md5
 */
public class Md5Properties {

    public static final String ALGORITHM = "MD5";
    public static final int SIGNUM = 1;
    public static final int RADIX = 16;
    public static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String charsetName = "utf-8";

    public String getCharsetName() {
        return charsetName;
    }

    public Md5Properties setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }
}
