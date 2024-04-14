package org.tools.encrypt.symmetrical.aes;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.exception.symmetrical.aes.AesException;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/05
 */
public class AesHelper {

    private AesHelper() {
    }

    @Getter
    private static AesProperties properties;

    public static void load(KeyPair keyPair) throws AesException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new AesException("keyPair 不能为空！");
        }
        AesProperties properties = new AesProperties();
        properties.setSecretKey(keyPair.getSecretKey());
        SecretKey secretKey = AesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setAesKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        load(properties);
    }

    public static void load(AesProperties properties) throws AesException {
        if (BaseUtils.isEmpty(properties)) {
            throw new AesException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getAesKey())) {
            throw new AesException("secretKey 不能为空！");
        }
        AesHelper.properties = properties;
    }

    public static String encode(String raw) throws AesException {
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getAesKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws AesException {
        SecretKey key;
        try {
            key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AesException(e);
        }
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws AesException {
        return AesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws AesException {
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getAesKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws AesException {
        SecretKey key;
        try {
            key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AesException(e);
        }
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws AesException {
        return AesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
