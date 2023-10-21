package org.tools.encrypt.asymmetric.rsa;

import org.junit.Test;
import org.tools.encrypt.asymmetric.rsa.factory.RsaFactory;
import org.tools.encrypt.exception.EncryptException;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 19:07
 * @description rsa测试类
 * @scope test
 */
public class RsaTest {

    private Rsa rsa;

    @Test
    public void test() throws EncryptException {

        rsa = RsaFactory.newInstance();

        RsaProperties rsaProperties = rsa.getRsaProperties();

//        rsa.getRsaProperties().setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDg+EI8egIBnZI3VqF3rG3bIbGpuA4JQtdXJ3/nYHAtjf07GF3WTyVZwOegqeIG5+ISslGuLe/i9B398x8WymzXCWg0HanqVdL52/tw+hKgOXoEKp8xb1NpmAeLO5l9TO4nYvZDNtKSm5HmSQzrtMjKiRmo/xPK+/2lulzL0LPIvwIDAQAB");
//        rsa.getRsaProperties().setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOD4Qjx6AgGdkjdWoXesbdshsam4DglC11cnf+dgcC2N/TsYXdZPJVnA56Cp4gbn4hKyUa4t7+L0Hf3zHxbKbNcJaDQdqepV0vnb+3D6EqA5egQqnzFvU2mYB4s7mX1M7idi9kM20pKbkeZJDOu0yMqJGaj/E8r7/aW6XMvQs8i/AgMBAAECgYAJfc/ll0wwX0KE/S2wy+CSpzhErdRBxcUqEyuNSn9RHLV7YcrLuOkdgUsEd0Zm/i+GlGlnzN5bF+uQBErifkXbNG8pv9ChdUIfZcfxwnhTY1s7N7I3D6fU/+IHoHTLcuG5PEC07KcXrDHvrCJKm4jHvBBAjzcanFNXoikMKV0zcQJBAPwFM+1zw8QX2fbFPVHxkcfS8+ekLVDRURAjMGf1ZJNNR3JJlQETA0B9BXIwXSdH0B9nXc+nPcDZ3inVZlVVOgcCQQDkhbQVsmAq/9rtg0aZNwMti2t3KK7zvk7/au1wEne/ibeq3sDJVCFWTaVM0b+L/I4xxprbwe6Xc8cOGtse162JAkEAv1a/5w8WWaH2NTno5XbvxHOwBHWMhih1UDg3PyZ7Nq7ciLO+8jLaLDL/WIPJpdIK/FzBR6/k3a9L0h0X9b+gLwJAHgze84NzVfQzK8x5eihbyJ9u0EqPRwodhUTgWD51dMB9YIDORStE/v5S4FksoRHi+PSu5HsKDuxwnpA1hEbEUQJAbstFSSJmeFoVL1cB2WozrPuXorb/o47AYdj3ERVhhEPZ6iCWQgMZRuYTL0HYzN2ES9H8nv5oF36qM0XExdJ53w==");

        System.out.println(rsaProperties.getPublicKey());
        System.out.println(rsaProperties.getPrivateKey());

        String raw = "Hello, World!";

        String encode = rsa.encode(raw);
        System.out.println("encode: " + encode);

        String decode = rsa.decode(encode);
        System.out.println("decode:" + decode);


        String sign = rsa.sign(raw);
        System.out.println("sign: " + sign);

        boolean verify = rsa.verify(decode, sign);
        System.out.println("verify: " + verify);

        rsa = RsaFactory.newInstance();

    }

}
