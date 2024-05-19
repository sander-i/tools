package org.tools.encrypt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    SHA256_WITH_DSA("SHA256withDSA"),
    MD5_WITH_RSA("MD5withRSA"),
    ;

    private final String signAlgorithm;

}
