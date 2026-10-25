package org.tools.encrypt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sander
 * @version 1.0
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
     * DSA
     */
    DSA("DSA"),

    ;

    private final String algorithm;

    /** 与算法名字符串比较 */
    public boolean equals(String algorithm) {
        return this.algorithm.equals(algorithm);
    }

    /** 按算法名查找枚举（不存在时抛 NoSuchElementException） */
    public static AlgorithmEnum enumName(String algorithm) {
        return Arrays.stream(AlgorithmEnum.values())
                .filter(algorithmEnum -> algorithmEnum.equals(algorithm))
                .findFirst()
                .get();
    }

}
