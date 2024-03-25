package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.http.HttpEnum;
import org.tools.bedrock.http.HttpResult;
import org.tools.bedrock.http.resp.R;
import org.tools.bedrock.http.resp.RR;

import java.util.HashMap;

/**
 * @description 描述
 * @scope 作用域
 */
public class HttpTest {

    HashMap<String, String> hashMap = new HashMap<>();

    {
        hashMap.put("1", "6");
    }

    public RR test() {
        return R.success(1, "成功", hashMap);
    }

//    public RR test1() {
//        PageBean<HashMap<String, String>> pageBean = new PageBean<>();
//        List<HashMap<String, String>> list = new ArrayList<>();
//        list.add(hashMap);
//        pageBean.setData(list);
//        return RQ.success(1, "成功", pageBean);
//    }

    @Test
    public void test01() {

        System.out.println(test());

//        System.out.println(test1());

        System.out.println(HttpResult.success(HttpEnum.OK));

    }

}
