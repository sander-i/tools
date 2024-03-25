package org.tools.encrypt.asymmetric.rsa;

import org.tools.encrypt.asymmetric.AsymmetricEncrypt;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:21
 * @description rsa非对称加密接口
 * @scope rsa
 */
@Deprecated
public interface Rsa extends AsymmetricEncrypt {

    /**
     * 获取配置文件
     *
     * @return RsaProperties
     */
    RsaProperties getRsaProperties();

    /**
     * 设置配置文件
     *
     * @param rsaProperties 配置
     * @return Rsa
     */
    Rsa setRsaProperties(RsaProperties rsaProperties);

}
