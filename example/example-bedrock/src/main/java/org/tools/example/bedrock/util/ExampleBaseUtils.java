package org.tools.example.bedrock.util;


import org.tools.bedrock.base.Address;
import org.tools.bedrock.util.BaseUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/05/02
 */
public class ExampleBaseUtils {

    public static void main(String[] args) {

        String str = "Hello, World!";

        Address address = new Address();

        if(BaseUtils.isEmptyObj(address)){
            System.out.println("address is empty");
        }
        address.setRemark("edfgh");

        if(BaseUtils.isEmptyObj(address)){
            System.out.println("address is empty");
        }

    }

}
