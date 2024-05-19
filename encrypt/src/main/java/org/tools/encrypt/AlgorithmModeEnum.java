package org.tools.encrypt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sander
 * @version 1.0
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
    RSA_NONE_NO_PADDING("RSA/None/NoPadding"),
    RSA_NONE_OAEPWITHSHA__256ANDMGF1PADDING("RSA/None/OAEPWITHSHA-256ANDMGF1PADDING"),
    RSA_ECB_OAEPWITHSHA256ANDMGF1PADDING("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"),

    /**
     * AES
     */
    AES("AES"),
    AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),

    /**
     * DIGITAL_SIGNATURE
     */
    MD5withRSA("MD5withRSA"),
    ;

    private final String algorithmMode;

    public static AlgorithmModeEnum enumName(String algorithmMode) {
        return Arrays.stream(AlgorithmModeEnum.values())
                .filter(algorithmModeEnum -> algorithmModeEnum.equals(algorithmMode))
                .findFirst()
                .get();
    }

    public boolean equals(String algorithmMode) {
        return this.algorithmMode.equals(algorithmMode);
    }

}
