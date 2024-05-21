package org.tools.example.bedrock.util;

import org.tools.bedrock.util.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/04/27
 */
public class ExampleLists {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        boolean b = Lists.oneRecord(list);
        System.out.println(b);
        list.remove(2);
        list.remove(1);
        b = Lists.oneRecord(list);
        System.out.println(list);
        System.out.println(b);
    }

}
