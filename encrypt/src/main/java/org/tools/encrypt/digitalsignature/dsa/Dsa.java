package org.tools.encrypt.digitalsignature.dsa;

import org.tools.encrypt.digitalsignature.DigitalsignatureEncrypt;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:11
 * @description dsa签名接口
 * @scope dsa
 */
@Deprecated
public interface Dsa extends DigitalsignatureEncrypt {

    /**
     * 获取配置文件
     *
     * @return 配置
     */
    DsaProperties getDsaProperties();

    /**
     * 设置配置文件
     *
     * @param dsaProperties 配置
     * @return Des
     */
    Dsa setDsaProperties(DsaProperties dsaProperties);

}
