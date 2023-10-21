package org.tools.encrypt.basic;

import org.junit.Test;
import org.tools.encrypt.basic.sha.Sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/14 17:57
 * @description 描述
 * @scope 作用域
 */
public class ShaTest {

    @Test
    public void test() throws NoSuchAlgorithmException {


        String encode = Sha.encode("SHA-512","666");
        System.out.println(encode);

    }

}
