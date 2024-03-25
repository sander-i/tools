package org.tools.encrypt.symmetrical.des.factory;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.exception.symmetrical.des.DesException;
import org.tools.encrypt.symmetrical.des.Des;
import org.tools.encrypt.symmetrical.des.DesKeyGenerator;
import org.tools.encrypt.symmetrical.des.DesProperties;
import org.tools.encrypt.symmetrical.des.impl.DesImpl;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/03 11:27
 * @description des加密工厂
 * @scope des
 */
@Deprecated
public class DesFactory {

    /**
     * 线程安全，调用效率高，可以延时加载
     * 静态内部类实现单例模式
     */
    private static class DesInstance {
        private static final Des DES = new DesImpl();
    }

    public static Des newInstance() throws DesException {
        return newInstance(DesKeyGenerator.getDesProperties());
    }

    public static Des newInstance(DesProperties desProperties) {
        if (BaseUtils.isNull(DesInstance.DES.getDesProperties())) {
            DesInstance.DES.setDesProperties(desProperties);
        }
        return DesInstance.DES;
    }

}
