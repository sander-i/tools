package org.tools.bedrock.util.date;

/**
 * @author sander
 * @version 1.0
 * @date 2023/12/23 14:32
 * @description 描述
 * @scope 作用域
 */
public class DateConstants {

    private DateConstants() {
    }

    /**
     * 年-月-日 时:分:秒 毫秒
     */
    public static final String YMD_HMS_S_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    /**
     * 年-月-日 时:分:秒
     */
    public static final String YMD_HMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年-月-日 时:分
     */
    public static final String YMD_HM_PATTERN = "yyyy-MM-dd HH:mm";
    /**
     * 年-月-日 时
     */
    public static final String YMD_H_PATTERN = "yyyy-MM-dd HH";
    /**
     * 年-月-日
     */
    public static final String Y_M_D_PATTERN = "yyyy-MM-dd";
    /**
     * 时:分:秒 毫秒
     */
    public static final String HMS_S_PATTERN = "HH:mm:ss SSS";
    /**
     * 时:分:秒
     */
    public static final String HMS_PATTERN = "HH:mm:ss";
    /**
     * 时:分
     */
    public static final String HM_PATTERN = "HH:mm";
    /**
     * 时
     */
    public static final String H_PATTERN = "HH";

    /**
     * 年月日
     */
    public static final String YMD_PATTERN = "yyyyMMdd";

    /**
     * 年-月-日 时:分:秒 12小时制(am/pm) 时区
     */
    public static final String YMD_HMS_A_Z_PATTERN = "yyyy-MM-dd HH:mm:ss a z";
    /**
     * 年-月-日 时:分 12小时制(am/pm) 时区
     */
    public static final String YMD_HM_A_Z_PATTERN = "yyyy-MM-dd HH:mm a z";
    /**
     * 年-月-日 时 12小时制(am/pm) 时区
     */
    public static final String YMD_H_A_Z_PATTERN = "yyyy-MM-dd HH a z";

    /**
     * 年-月-日 时:分:秒 12小时制(am/pm)
     */
    public static final String YMD_HMS_A_PATTERN = "yyyy-MM-dd HH:mm:ss a";
    /**
     * 年-月-日 时:分 12小时制(am/pm)
     */
    public static final String YMD_HM_A_PATTERN = "yyyy-MM-dd HH:mm a";
    /**
     * 年-月-日 时 12小时制(am/pm)
     */
    public static final String YMD_H_A_PATTERN = "yyyy-MM-dd HH a";

    /**
     * 年-月-日 时:分:秒 时区
     */
    public static final String YMD_HMS_Z_PATTERN = "yyyy-MM-dd HH:mm:ss z";
    /**
     * 年-月-日 时:分 时区
     */
    public static final String YMD_HM_Z_PATTERN = "yyyy-MM-dd HH:mm z";
    /**
     * 年-月-日 时 时区
     */
    public static final String YMD_H_Z_PATTERN = "yyyy-MM-dd HH z";

}
