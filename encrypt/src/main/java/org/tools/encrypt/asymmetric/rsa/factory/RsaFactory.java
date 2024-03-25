package org.tools.encrypt.asymmetric.rsa.factory;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.asymmetric.rsa.Rsa;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;
import org.tools.encrypt.asymmetric.rsa.RsaProperties;
import org.tools.encrypt.asymmetric.rsa.impl.RsaImpl;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 17:34
 * @description rsa加密工厂
 * @scope rsa
 */
@Deprecated
public class RsaFactory {

    /**
     * 线程安全，调用效率高，可以延时加载
     * 静态内部类实现单例模式
     */
    private static class RsaInstance {
        private static final Rsa RSA = new RsaImpl();
    }

    public static Rsa newInstance() throws RsaException {
        return newInstance(RsaKeyGenerator.getRsaProperties());
    }

    public static Rsa newInstance(RsaProperties rsaProperties) {
        if (BaseUtils.isNull(RsaInstance.RSA.getRsaProperties())) {
            RsaInstance.RSA.setRsaProperties(rsaProperties);
        }
        return RsaInstance.RSA;
    }

}
