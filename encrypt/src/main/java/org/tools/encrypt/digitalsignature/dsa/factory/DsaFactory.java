package org.tools.encrypt.digitalsignature.dsa.factory;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.digitalsignature.dsa.Dsa;
import org.tools.encrypt.digitalsignature.dsa.DsaKeyGenerator;
import org.tools.encrypt.digitalsignature.dsa.DsaProperties;
import org.tools.encrypt.digitalsignature.dsa.impl.DsaImpl;
import org.tools.encrypt.exception.digitalsignature.dsa.DsaException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/13 17:37
 * @description dsa签名工厂
 * @scope dsa
 */
public class DsaFactory {

    /**
     * 线程安全，调用效率高，可以延时加载
     * 静态内部类实现单例模式
     */
    private static class DsaInstance {
        private static final Dsa DSA = new DsaImpl();
    }

    public static Dsa newInstance() throws DsaException {
        return newInstance(DsaKeyGenerator.getDsaProperties());
    }

    public static Dsa newInstance(DsaProperties dsaProperties) {
        if (BaseUtils.isNull(DsaInstance.DSA.getDsaProperties())) {
            DsaInstance.DSA.setDsaProperties(dsaProperties);
        }
        return DsaInstance.DSA;
    }

}
