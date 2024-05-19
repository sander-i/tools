package org.tools.spring.boot.encrypt.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.AlgorithmEnum;
import org.tools.encrypt.asymmetric.rsa.RsaHelper;
import org.tools.encrypt.asymmetric.rsa.RsaProperties;
import org.tools.encrypt.entity.Encrypt;
import org.tools.encrypt.entity.asymmetric.Asymmetric;
import org.tools.encrypt.entity.asymmetric.Rsa;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.entity.symmetrical.Des;
import org.tools.encrypt.entity.symmetrical.Symmetrical;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.symmetrical.aes.AesHelper;
import org.tools.encrypt.symmetrical.aes.AesProperties;
import org.tools.encrypt.symmetrical.des.DesHelper;
import org.tools.encrypt.symmetrical.des.DesProperties;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/10 22:36
 * @description des 自动配置类
 * @scope spring boot
 */
@Slf4j
// 配置类
@Configuration
// 存在 Encrypt 类时初始化该配置类
@ConditionalOnClass({Encrypt.class})
// 开启配置属性支持
@EnableConfigurationProperties({Encrypt.class, Asymmetric.class, Symmetrical.class, Rsa.class, Aes.class, Des.class})
//@EnableConfigurationProperties({Encrypt.class})
// 扫描 Encrypt 类所在包
@ComponentScan(basePackageClasses = Encrypt.class)
// 存在对应配置信息时初始化该配置类
@ConditionalOnProperty(prefix = "tools", value = "encrypt.enabled", havingValue = "true")
public class EncryptAutoConfiguration {

    @Autowired
    private Encrypt encrypt;

    @Bean
    @ConditionalOnMissingBean(type = "rsaTemplate")
    @ConditionalOnProperty(prefix = "tools.encrypt.asymmetric.rsa", value = "enabled", havingValue = "true")
    public RsaProperties rsa() {
        final String checkEncrypt = checkEncrypt(false, AlgorithmEnum.AES);
        if (BaseUtils.isNotEmpty(checkEncrypt)) {
            return null;
        }
        try {
            RsaHelper.load(encrypt.getAsymmetric().getRsa());
        } catch (EncryptException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
            log.error("EncryptAutoConfiguration rsa error", e);
        }
        return RsaHelper.getProperties();
    }

    @Bean
    @ConditionalOnMissingBean(type = "desTemplate")
    @ConditionalOnProperty(prefix = "tools.encrypt.symmetrical.des", value = "enabled", havingValue = "true")
    public DesProperties des() {
        final String checkEncrypt = checkEncrypt(true, AlgorithmEnum.DES);
        if (BaseUtils.isNotEmpty(checkEncrypt)) {
            return null;
        }
        try {
            DesHelper.load(encrypt.getSymmetrical().getDes());
        } catch (EncryptException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            log.error("EncryptAutoConfiguration des error", e);
        }
        return DesHelper.getProperties();
    }

    @Bean
    @ConditionalOnMissingBean(type = "aesTemplate")
    @ConditionalOnProperty(prefix = "tools.encrypt.symmetrical.aes", value = "enabled", havingValue = "true")
    public AesProperties aes() {
        final String checkEncrypt = checkEncrypt(true, AlgorithmEnum.AES);
        if (BaseUtils.isNotEmpty(checkEncrypt)) {
            return null;
        }
        try {
            AesHelper.load(encrypt.getSymmetrical().getAes());
        } catch (EncryptException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            log.error("EncryptAutoConfiguration des error", e);
        }
        return AesHelper.getProperties();
    }

    /**
     * 检查加密配置
     *
     * @param isSymmetrical 是否对称加密
     * @param algorithmEnum 算法枚举
     * @return 加密配置错误信息
     */
    private String checkEncrypt(boolean isSymmetrical, AlgorithmEnum algorithmEnum) {
        log.debug("EncryptAutoConfiguration checkEncrypt encrypt: {}", encrypt);
        if (Objects.isNull(encrypt)) {
            log.error("EncryptAutoConfiguration {} error", algorithmEnum.getAlgorithm());
            return String.format("EncryptAutoConfiguration %s error", algorithmEnum.getAlgorithm());
        }
        final Symmetrical symmetrical = encrypt.getSymmetrical();
        final Asymmetric asymmetric = encrypt.getAsymmetric();
        log.debug("EncryptAutoConfiguration checkEncrypt isSymmetrical: {}, algorithmEnum: {}", isSymmetrical, algorithmEnum.getAlgorithm());
        if (isSymmetrical) {
            // 对称加密
            if (Objects.isNull(symmetrical)) {
                log.error("EncryptAutoConfiguration symmetrical error");
                return "EncryptAutoConfiguration symmetrical error";
            }
        } else {
            // 非对称加密
            if (Objects.isNull(asymmetric)) {
                log.error("EncryptAutoConfiguration asymmetric error");
                return "EncryptAutoConfiguration asymmetric error";
            }
        }
        switch (algorithmEnum) {
            case RSA:
                final Rsa rsa = asymmetric.getRsa();
                if (Objects.isNull(rsa)) {
                    log.error("EncryptAutoConfiguration rsa error");
                    return "EncryptAutoConfiguration rsa error";
                }
                break;
            case DES:
                final Des des = symmetrical.getDes();
                if (Objects.isNull(des)) {
                    log.error("EncryptAutoConfiguration des error");
                    return "EncryptAutoConfiguration des error";
                }
                break;
            case AES:
                final Aes aes = symmetrical.getAes();
                if (Objects.isNull(aes)) {
                    log.error("EncryptAutoConfiguration aes error");
                    return "EncryptAutoConfiguration aes error";
                }
                break;
            default:
                log.error("EncryptAutoConfiguration {} error", algorithmEnum.getAlgorithm());
                break;
        }
        return "";
    }

}
