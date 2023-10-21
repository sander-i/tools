package org.tools.encrypt.digitalsignature.dsa;

import org.junit.Test;
import org.tools.encrypt.digitalsignature.dsa.factory.DsaFactory;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 16:49
 * @description 描述
 * @scope 作用域
 */
public class DsaTest {

    Dsa dsa;

    @Test
    public void test() throws EncryptException {

        dsa = DsaFactory.newInstance();

        String raw = "树枝666";

        String sign = dsa.sign(raw);
        System.out.println("sign: " + sign);

        Boolean verify = dsa.verify(raw, sign);
        System.out.println("verify: " + verify);

    }

}
