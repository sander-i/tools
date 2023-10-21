package org.tools.spring.boot.encrypt.symmetrical.des;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tools.encrypt.exception.EncryptException;
import org.tools.encrypt.symmetrical.des.Des;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/10 22:52
 * @description 描述
 * @scope 作用域
 */
public class DesTest {

    @Autowired
    private Des des;

    @Test
    public void test() throws EncryptException {

        String encode = des.encode("Hello, World!");
        System.out.println(encode);

        String decode = des.decode(encode);
        System.out.println(decode);

    }

}
