package org.tools.encrypt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 算法模式枚举：各算法的完整转换模式（如 AES/CBC/PKCS5Padding）
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@Getter
@AllArgsConstructor
public enum AlgorithmModeEnum {
    MD5("MD5"),

    /**
     * SHA
     */
    SHA("SHA"),

    SHA1("SHA-1"),

    SHA256("SHA-256"),

    SHA384("SHA-384"),

    SHA512("SHA-512"),

    /**
     * DES
     */
    DES("DES"),

    DES_ECB_NO_PADDING("DES/ECB/NoPadding"),

    DES_EDE_CBC_PKCS5Padding("DESede/CBC/PKCS5Padding"),

    /**
     * RSA
     */
    RSA("RSA"),

    RSA_ECB_OAEPWITHSHA__256ANDMGF1PADDING("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"),

    OAEPWithSHA__256AndMGF1Padding("OAEPWithSHA-256AndMGF1Padding"),

    /**
     * AES
     */
    AES("AES"),

    AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),

    /**
     * IDEA
     */
    IDEA("IDEA"),

    IDEA_ECB_ISO10126Padding("IDEA/ECB/ISO10126Padding"),

    /**
     * Blowfish
     */
    BLOWFISH("Blowfish"),

    BLOWFISH_CBC_PKCS5PADDING("Blowfish/CBC/PKCS5Padding"),

    /**
     * RC
     */
    RC2("RC2"),

    RC2_CBC_PKCS5PADDING("RC2/CBC/PKCS5Padding"),

    RC4("RC4"),

    RC5("RC5"),

    RC5_CBC_PKCS5PADDING("RC5/CBC/PKCS5Padding"),

    /**
     * SKIPJACK
     */
    SKIPJACK("SKIPJACK"),

    SKIPJACK_CBC_PKCS5PADDING("SKIPJACK/CBC/PKCS5Padding"),

    /**
     * DIGITAL_SIGNATURE
     */
    MD5withRSA("MD5withRSA"),
    ;

    private final String algorithmMode;

    /**
     * 与算法模式字符串比较
     *
     * @param algorithmMode 待比较的算法模式名
     * @return 相等返回 true，否则返回 false
     */
    public boolean equals(String algorithmMode) {
        return this.algorithmMode.equals(algorithmMode);
    }

    /**
     * 按算法模式名查找枚举（不存在时抛 NoSuchElementException）
     *
     * @param algorithmMode 算法模式名
     * @return 匹配的枚举
     */
    public static AlgorithmModeEnum enumName(String algorithmMode) {
        return Arrays.stream(AlgorithmModeEnum.values())
                .filter(algorithmModeEnum -> algorithmModeEnum.equals(algorithmMode))
                .findFirst()
                .get();
    }
}
