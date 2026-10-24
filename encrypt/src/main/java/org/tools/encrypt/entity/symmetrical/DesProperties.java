package org.tools.encrypt.entity.symmetrical;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tools.encrypt.entity.EncryptProperties;
import org.tools.encrypt.enums.AlgorithmEnum;
import org.tools.encrypt.enums.AlgorithmModeEnum;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * des加密配置文件
 *
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DesProperties extends EncryptProperties {

    /**
     * DES密钥
     */
    private String secretKeyStr;

    /**
     * DES 密钥对象
     */
    private SecretKey secretKey;

    {
        setAlgorithmMode(AlgorithmModeEnum.DES.getAlgorithmMode());
        setAlgorithm(AlgorithmEnum.DES.getAlgorithm());
        setKeyBits(56);
    }

    /**
     * 无密钥对象时的兜底处理：按 DES 密钥约定（UTF-8 原文，恰好 8 字节）从
     * {@code secretKeyStr} 构造 {@code secretKey}。
     *
     * <p>适用场景：调用方只有字符串密钥（如 yml 配置）时，先调用本方法再交给
     * {@code DesHelper.load(DesProperties)} 装载；{@code DesHelper.load} 在
     * {@code secretKey} 为空且 {@code secretKeyStr} 非空时会自动调用本方法。</p>
     */
    public void setDesKeyFixedLength() {
        byte[] secretKeyBytes = secretKeyStr.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(secretKeyBytes, algorithm);
    }

}
