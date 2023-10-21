package org.tools.encrypt.symmetrical.des;

import org.junit.Test;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.symmetrical.des.factory.DesFactory;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/10 17:16
 * @description des测试类
 * @scope test
 */
public class DesTest {

    private Des des;

    @Test
    public void test() throws EncryptException {

        des = DesFactory.newInstance();

        String encode = des.encode("Hello, World!");
        System.out.println(encode);

        String decode = des.decode(encode);
        System.out.println(decode);

    }

}
