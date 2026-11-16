package io.github.sander_i.encrypt.enums;

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
    /**
     * SHA3-256withRSA 需 BouncyCastle provider，JDK 8 原生不提供
     */
    SHA3_256_WITH_RSA("SHA3-256withRSA"),
    SHA256_WITH_ECDSA("SHA256withECDSA"),
    SHA384_WITH_ECDSA("SHA384withECDSA"),
    SHA512_WITH_ECDSA("SHA512withECDSA"),
    ;

    private final String signAlgorithm;

    public boolean equals(String signAlgorithm) {
        return this.signAlgorithm.equals(signAlgorithm);
    }

    public static SignAlgorithmEnum enumName(String signAlgorithm) {
        return Arrays.stream(SignAlgorithmEnum.values())
                .filter(algorithmModeEnum -> algorithmModeEnum.equals(signAlgorithm))
                .findFirst()
                .get();
    }

}
