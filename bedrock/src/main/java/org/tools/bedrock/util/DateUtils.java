package org.tools.bedrock.util;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:37
 * @description 日期类型工具
 * @scope all
 */
@Log4j2
public class DateUtils {

    private DateUtils() {
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
     * 年-月-日
     */
    public static final String Y_M_D_PATTERN = "yyyy-MM-dd";
    /**
     * 年月日
     */
    public static final String YMD_PATTERN = "yyyyMMdd";
    /**
     * 时:分:秒 毫秒
     */
    public static final String HMS_S_PATTERN = "HH:mm:ss SSS";
    /**
     * 时:分:秒
     */
    public static final String HMS_PATTERN = "HH:mm:ss";

    /**
     * 字符串转日期(自动匹配格式)
     *
     * @param dateStr 字符串
     * @return Date
     */
    public static Date strDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_HMS_S_PATTERN);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception ymdHmsS) {
            sdf = new SimpleDateFormat(YMD_HMS_PATTERN);
            try {
                date = sdf.parse(dateStr);
            } catch (Exception ymdHms) {
                sdf = new SimpleDateFormat(Y_M_D_PATTERN);
                try {
                    date = sdf.parse(dateStr);
                } catch (Exception ymd) {
                    sdf = new SimpleDateFormat(HMS_S_PATTERN);
                    try {
                        date = sdf.parse(dateStr);
                    } catch (Exception hmsS) {
                        sdf = new SimpleDateFormat(HMS_PATTERN);
                        try {
                            date = sdf.parse(dateStr);
                        } catch (Exception hms) {
                            log.error("字符串转日期(自动匹配格式)", hms);
                        }
                    }
                }
            }
        }
        return date;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr 字符串
     * @param pattern 日期、时间类型
     * @return Date
     */
    public static Date strDateFormat(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("字符串转日期", e);
        }
        return null;
    }

    /**
     * 日期转字符串格式化
     *
     * @param date 日期
     * @return String 格式化日期
     */
    public static String dateFormatStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_HMS_S_PATTERN);
        String dateStr = null;
        try {
            dateStr = sdf.format(date);
        } catch (Exception ymdHmsS) {
            sdf = new SimpleDateFormat(YMD_HMS_PATTERN);
            try {
                dateStr = sdf.format(date);
            } catch (Exception ymdHms) {
                sdf = new SimpleDateFormat(Y_M_D_PATTERN);
                try {
                    dateStr = sdf.format(date);
                } catch (Exception ymd) {
                    sdf = new SimpleDateFormat(HMS_S_PATTERN);
                    try {
                        dateStr = sdf.format(date);
                    } catch (Exception hmsS) {
                        sdf = new SimpleDateFormat(HMS_PATTERN);
                        try {
                            dateStr = sdf.format(date);
                        } catch (Exception hms) {
                            log.error("日期转字符串格式化", hms);
                        }
                    }
                }
            }
        }
        return dateStr;
    }

    /**
     * 日期转字符串格式化
     *
     * @param date 日期
     * @return String 格式化日期
     */
    public static String dateFormatStr(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 截取生日
     *
     * @param idNo 证件号
     * @return boolean
     */
    public static Date subBirthday(String idNo) {
        return DateUtils.strDateFormat(idNo.substring(6, 14), DateUtils.YMD_PATTERN);
    }

    /**
     * 获取当前的UTC时间
     *
     * @return Date
     */
    public static Date getUtc() {
        return Date.from(Instant.now());
    }

}
