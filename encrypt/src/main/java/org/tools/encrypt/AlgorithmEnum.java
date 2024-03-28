package org.tools.encrypt;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    SHA512("SHA-512");

    private final String algorithm;

    public boolean equals(String algorithm) {
        return this.algorithm.equals(algorithm);
    }

}
