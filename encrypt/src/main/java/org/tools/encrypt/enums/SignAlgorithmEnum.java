package org.tools.encrypt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * <p>
 * 验签算法
 * </p>
 *
 * @author sander 2024/04/14
 */
@Getter
@RequiredArgsConstructor
public enum SignAlgorithmEnum {

    MD5_WITH_RSA("MD5withRSA"),

    SHA256_WITH_RSA("SHA256withRSA"),

    SHA256_WITH_DSA("SHA256withDSA"),

    SHA3_256_WITH_RSA("SHA3-256withRSA"),

    ;

    private final String signAlgorithm;

    /** 与签名算法字符串比较 */
    public boolean equals(String signAlgorithm) {
        return this.signAlgorithm.equals(signAlgorithm);
    }

    /** 按签名算法名查找枚举（不存在时抛 NoSuchElementException） */
    public static SignAlgorithmEnum enumName(String signAlgorithm) {
        return Arrays.stream(SignAlgorithmEnum.values())
                .filter(algorithmModeEnum -> algorithmModeEnum.equals(signAlgorithm))
                .findFirst()
                .get();
    }

}
