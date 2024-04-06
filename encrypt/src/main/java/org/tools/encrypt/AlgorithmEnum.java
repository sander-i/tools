package org.tools.encrypt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/28 21:37
 * @description 描述
 * @scope 作用域
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
     * DIGITAL_SIGNATURE
     */
    MD5withRSA("MD5withRSA"),
    ;

    private final String algorithm;

    public boolean equals(String algorithm) {
        return this.algorithm.equals(algorithm);
    }

    public static AlgorithmEnum enumName(String algorithm) {
        return Arrays.stream(AlgorithmEnum.values())
                .filter(algorithmEnum -> algorithmEnum.equals(algorithm))
                .findFirst()
                .get();
    }

}
