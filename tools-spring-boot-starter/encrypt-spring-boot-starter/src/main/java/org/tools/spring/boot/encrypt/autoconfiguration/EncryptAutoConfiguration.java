package org.tools.spring.boot.encrypt.autoconfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.asymmetric.rsa.RsaHelper;
import org.tools.encrypt.entity.Encrypt;
import org.tools.encrypt.entity.EncryptScanPackage;
import org.tools.encrypt.entity.asymmetric.Asymmetric;
import org.tools.encrypt.entity.asymmetric.Rsa;
import org.tools.encrypt.entity.asymmetric.RsaProperties;
import org.tools.encrypt.entity.symmetrical.Aes;
import org.tools.encrypt.entity.symmetrical.AesProperties;
import org.tools.encrypt.entity.symmetrical.Des;
import org.tools.encrypt.entity.symmetrical.DesProperties;
import org.tools.encrypt.entity.symmetrical.Idea;
import org.tools.encrypt.entity.symmetrical.IdeaProperties;
import org.tools.encrypt.entity.symmetrical.Symmetrical;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.symmetrical.aes.AesHelper;
import org.tools.encrypt.symmetrical.des.DesHelper;
import org.tools.encrypt.symmetrical.idea.IdeaHelper;
import org.tools.spring.boot.encrypt.OnEncryptPropertiesCondition;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/**
 * 自动配置类
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
@Configuration
@ConditionalOnClass({Encrypt.class})
@Conditional(OnEncryptPropertiesCondition.class)
@ConfigurationPropertiesScan(basePackages = EncryptScanPackage.ENTITY)
@RequiredArgsConstructor
public class EncryptAutoConfiguration {

    private final Encrypt encrypt;

    static {
        printWelcome();
    }

    @Bean
    @ConditionalOnMissingBean(type = "rsaTemplate")
    @ConditionalOnProperty(prefix = "tools.encrypt.asymmetric.rsa", value = "enabled", havingValue = "true")
    public RsaProperties rsa() {
        final String checkEncrypt = checkEncrypt(false, AlgorithmEnum.RSA);
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
        } catch (EncryptException e) {
            log.error("EncryptAutoConfiguration des error", e);
        }
        return AesHelper.getProperties();
    }

    @Bean
    @ConditionalOnMissingBean(type = "ideaTemplate")
    @ConditionalOnProperty(prefix = "tools.encrypt.symmetrical.idea", value = "enabled", havingValue = "true")
    public IdeaProperties idea() {
        final String checkEncrypt = checkEncrypt(true, AlgorithmEnum.IDEA);
        if (BaseUtils.isNotEmpty(checkEncrypt)) {
            return null;
        }
        try {
            IdeaHelper.load(encrypt.getSymmetrical().getIdea());
        } catch (EncryptException e) {
            log.error("EncryptAutoConfiguration idea error", e);
        }
        return IdeaHelper.getProperties();
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
            case IDEA:
                final Idea idea = symmetrical.getIdea();
                if (Objects.isNull(idea)) {
                    log.error("EncryptAutoConfiguration idea error");
                    return "EncryptAutoConfiguration idea error";
                }
                break;
            default:
                log.error("EncryptAutoConfiguration {} error", algorithmEnum.getAlgorithm());
                break;
        }
        return "";
    }

    private static void printWelcome() {
        final String[] banner = {
                "\n" +
                        "  _____           _     \n" +
                        " |_   _|__   ___ | |___ \n" +
                        "   | |/ _ \\ / _ \\| / __|\n" +
                        "   | | (_) | (_) | \\__ \\\n" +
                        "   |_|\\___/ \\___/|_|___/\n" +
                        "               1.0.6.5-d01\n" +
                        "\n"
        };

        String fullBanner = String.join("\n", banner);
        System.out.print(fullBanner);
//        System.out.print("\033[32m" + " :: Tools :: " + "\033[0m" + "\n\n");
    }

}
