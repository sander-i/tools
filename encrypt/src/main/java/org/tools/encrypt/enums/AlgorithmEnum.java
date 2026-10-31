package org.tools.encrypt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 算法枚举：摘要、对称、非对称与签名算法名及其匹配方法。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.6.6
 */
@Getter
@AllArgsConstructor
public enum AlgorithmEnum {
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

    DES_EDE("DESede"),

    /**
     * RSA
     */
    RSA("RSA"),

    /**
     * AES
     */
    AES("AES"),

    /**
     * IDEA
     */
    IDEA("IDEA"),

    /**
     * Blowfish
     */
    BLOWFISH("Blowfish"),

    /**
     * RC
     */
    RC2("RC2"),

    RC4("RC4"),

    RC5("RC5"),

    /**
     * SKIPJACK
     */
    SKIPJACK("SKIPJACK"),

    /**
     * DH
     */
    DH("DH"),

    /**
     * EC
     */
    EC("EC"),

    /**
     * DSA
     */
    DSA("DSA"),

    ;

    private final String algorithm;

    /**
     * 与算法名字符串比较
     *
     * @param algorithm 待比较的算法名
     * @return 相等返回 true，否则返回 false
     */
    public boolean equals(String algorithm) {
        return this.algorithm.equals(algorithm);
    }

    /**
     * 按算法名查找枚举（不存在时抛 NoSuchElementException）
     *
     * @param algorithm 算法名
     * @return 匹配的枚举
     */
    public static AlgorithmEnum enumName(String algorithm) {
        return Arrays.stream(AlgorithmEnum.values())
                .filter(algorithmEnum -> algorithmEnum.equals(algorithm))
                .findFirst()
                .get();
    }
}
