package org.tools.encrypt.basic.md5;

import org.tools.bedrock.util.BaseUtils;
import org.tools.encrypt.basic.md5.impl.Md5Impl;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 15:47
 * @description md5 帮助类
 * @scope md5
 */
public class Md5Helper {

    protected static Md5Properties md5Properties;
    protected static Md5 md5;

    public static Md5Properties getMd5Properties() {
        if (BaseUtils.isEmpty(md5Properties)) {
            md5Properties = new Md5Properties();
        }
        return md5Properties;
    }

    public static void setMd5Properties(Md5Properties md5Properties) {
        Md5Helper.md5Properties = md5Properties;
    }

    public static Md5 getMd5() {
        if (BaseUtils.isEmpty(md5)) {
            md5 = new Md5Impl();
            md5.setMd5Properties(getMd5Properties());
        }
        return md5;
    }

    public static void setMd5(Md5 md5) {
        Md5Helper.md5 = md5;
    }

}
