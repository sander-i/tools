package io.github.sander_i.encrypt.enums;

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
    DES_EDE_CBC_PKCS5PADDING("DESede/CBC/PKCS5Padding"),
    DES_EDE_ECB_PKCS5PADDING("DESede/ECB/PKCS5Padding"),

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
     * Blowfish
     */
    BLOWFISH("Blowfish"),
    BLOWFISH_ECB_PKCS5PADDING("Blowfish/ECB/PKCS5Padding"),
    BLOWFISH_CBC_PKCS5PADDING("Blowfish/CBC/PKCS5Padding"),

    /**
     * RC2
     */
    RC2("RC2"),
    RC2_ECB_PKCS5PADDING("RC2/ECB/PKCS5Padding"),
    RC2_CBC_PKCS5PADDING("RC2/CBC/PKCS5Padding"),

    /**
     * RC4（流加密，无分组模式）
     */
    RC4("RC4"),
    ARCFOUR("ARCFOUR"),

    /**
     * ECC / ECDH / ECDSA
     */
    EC("EC"),
    ECDH("ECDH"),
    SHA256withECDSA("SHA256withECDSA"),
    SHA384withECDSA("SHA384withECDSA"),
    SHA512withECDSA("SHA512withECDSA"),

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
