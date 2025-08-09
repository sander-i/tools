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
    @Deprecated
    RSA_ECB_NO_PADDING("RSA/ECB/NoPadding"),
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
     * DIGITAL_SIGNATURE
     */
    MD5withRSA("MD5withRSA"),
    ;

    private final String algorithmMode;

    public boolean equals(String algorithmMode) {
        return this.algorithmMode.equals(algorithmMode);
    }

    public static AlgorithmModeEnum enumName(String algorithmMode) {
        return Arrays.stream(AlgorithmModeEnum.values())
                .filter(algorithmModeEnum -> algorithmModeEnum.equals(algorithmMode))
                .findFirst()
                .get();
    }

}
