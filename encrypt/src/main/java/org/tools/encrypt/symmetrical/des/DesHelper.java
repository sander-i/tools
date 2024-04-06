package org.tools.encrypt.symmetrical.des;

import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.exception.EncryptException;

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
public class DesHelper {

    private DesHelper() {
    }

    @Getter
    private static DesProperties properties;

    public static void load(KeyPair keyPair) throws EncryptException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new EncryptException("keyPair 不能为空！");
        }
        DesProperties properties = new DesProperties();
        properties.setSecretKey(keyPair.getSecretKey());
        SecretKey secretKey = DesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setDesKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setIvParameter(keyPair.getIvParameter());
        load(properties);
    }

    public static void load(DesProperties properties) throws EncryptException {
        if (BaseUtils.isEmpty(properties)) {
            throw new EncryptException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getDesKey())) {
            throw new EncryptException("secretKey 不能为空！");
        }
        DesHelper.properties = properties;
    }

    public static String encode(String raw) throws EncryptException {
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getDesKey(), properties.getIvParameter(), raw);
    }

    public static String encode(String secretKey, String raw) throws EncryptException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptException(e);
        }
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws EncryptException {
        return DesUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), raw);
    }

    public static String decode(String wrought) throws EncryptException {
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getDesKey(), properties.getIvParameter(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws EncryptException {
        SecretKey key;
        try {
            key = DesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptException(e);
        }
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, properties.getIvParameter(), wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws EncryptException {
        return DesUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, properties.getIvParameter(), wrought);
    }

}
