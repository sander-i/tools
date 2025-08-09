package org.tools.encrypt.symmetrical.idea;


import lombok.Getter;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.entity.KeyPair;
import org.tools.encrypt.entity.symmetrical.Idea;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.exception.symmetrical.idea.IdeaException;
import org.tools.encrypt.symmetrical.aes.AesKeyGenerator;

import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * @author sander
 **/
public class IdeaHelper {

    private IdeaHelper() {
    }

    @Getter
    private static IdeaProperties properties = new IdeaProperties();

    private static void init(Idea aes, KeyPair keyPair) {
        if (BaseUtils.isEmpty(keyPair.getKeyBits())) {
            keyPair.setKeyBits(aes.getKeyBits());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithm())) {
            keyPair.setAlgorithm(aes.getAlgorithm());
        }
        if (BaseUtils.isEmpty(keyPair.getAlgorithmMode())) {
            keyPair.setAlgorithmMode(aes.getAlgorithmMode());
        }
        if (BaseUtils.isEmpty(keyPair.getSignAlgorithm())) {
            keyPair.setSignAlgorithm(aes.getSignAlgorithm());
        }
    }

    public static void load(KeyPair keyPair, String name) throws IdeaException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new IdeaException("keyPair 不能为空！");
        }
        if (!(keyPair instanceof Idea)) {
            throw new IdeaException("load(KeyPair keyPair, String name) 需要传入 KeyPair 子类 Idea ！");
        }
        Idea idea = (Idea) keyPair;
        if (!idea.getEnabled()) {
            throw new IdeaException("Idea enable: false");
        }
        if (idea.getGroupEnabled()) {
            List<KeyPair> groupList = idea.getGroup();
            for (KeyPair group : groupList) {
                if (name.equals(group.getName())) {
                    init(idea, group);
                    load(group);
                    return;
                }
            }
        }
        load(keyPair);
    }

    public static void load(KeyPair keyPair) throws IdeaException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        if (BaseUtils.isEmpty(keyPair)) {
            throw new IdeaException("keyPair 不能为空！");
        }
        IdeaProperties properties = new IdeaProperties();
        properties.setSecretKeyStr(keyPair.getSecretKey());
        SecretKey secretKey = AesKeyGenerator.getSecretKey(keyPair.getAlgorithm(), keyPair.getSecretKey());
        properties.setSecretKey(secretKey);
        properties.setAlgorithm(keyPair.getAlgorithm());
        properties.setAlgorithmMode(keyPair.getAlgorithmMode());
        properties.setKeyBits(keyPair.getKeyBits());
        load(properties);
    }

    public static void load(IdeaProperties properties) throws IdeaException {
        if (BaseUtils.isEmpty(properties)) {
            throw new IdeaException("properties 不能为空！");
        }
        if (BaseUtils.isEmpty(properties.getSecretKey())) {
            throw new IdeaException("secretKey 不能为空！");
        }
        IdeaHelper.properties = properties;
    }

    public static String encode(String raw) throws IdeaException {
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), raw);
    }

    public static String encode(String secretKey, String raw) throws IdeaException {
        SecretKey key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, raw);
    }

    public static String encode(SecretKey secretKey, String raw) throws IdeaException {
        return IdeaUtils.encode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, raw);
    }

    public static String decode(String wrought) throws IdeaException {
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), properties.getSecretKey(), wrought);
    }

    public static String decode(String secretKey, String wrought) throws IdeaException {
        SecretKey key = AesKeyGenerator.getSecretKey(properties.getAlgorithm(), secretKey);
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), key, wrought);
    }

    public static String decode(SecretKey secretKey, String wrought) throws IdeaException {
        return IdeaUtils.decode(properties.getAlgorithm(), properties.getAlgorithmMode(), secretKey, wrought);
    }

}
