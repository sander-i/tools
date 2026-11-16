package io.github.sander_i.example.bedrock.util;


import io.github.sander_i.bedrock.base.Address;
import io.github.sander_i.bedrock.util.BaseUtils;

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

        Address address = null;

        if(BaseUtils.isEmptyObj(address)){
            System.out.println("address is empty");
        }
        address.setRemark("edfgh");

        if(BaseUtils.isEmptyObj(address)){
            System.out.println("address is empty");
        }

    }

}
