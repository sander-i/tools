package org.tools.encrypt.symmetrical.des;

import org.tools.encrypt.symmetrical.SymmetricalEncrypt;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:14
 * @description des加密接口
 * @scope des
 */
public interface Des extends SymmetricalEncrypt {

    /**
     * 获取配置文件
     *
     * @return 配置
     */
    DesProperties getDesProperties();

    /**
     * 设置配置文件
     *
     * @param desProperties 配置
     * @return Des
     */
    Des setDesProperties(DesProperties desProperties);

}
