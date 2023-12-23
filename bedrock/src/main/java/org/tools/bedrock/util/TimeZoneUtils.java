package org.tools.bedrock.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xietao
 * @version 1.0
 * @date 2023/11/11 20:48
 * @description 时区工具类
 * @scope all
 * @deprecated This class is deprecated and should not be used anymore. Recommended use util.date.TimeZoneUtils.
 */
@Slf4j
@Deprecated
public class TimeZoneUtils {

    private TimeZoneUtils() {
    }

    private static final String UTC = "UTC";

    /**
     * 转换时区
     *
     * @param date           日期 YYYY-mm-DD HH:mm:ss
     * @param dateFormat     日期格式 "YYYY-mm-DD HH:mm:ss"
     * @param targetTimeZone 目标时区 Asia/Shanghai
     * @return Date
     * @throws ParseException 解析日期异常
     * @deprecated This method is deprecated.
     */
    @Deprecated
    public static Date convertTimeZone(Date date, String dateFormat, String targetTimeZone) throws ParseException {
        String dateStr = DateUtils.dateFormatStr(date, dateFormat);
        SimpleDateFormat bjSdf = new SimpleDateFormat(dateFormat);
        bjSdf.setTimeZone(TimeZone.getTimeZone(targetTimeZone));
        return bjSdf.parse(dateStr);
    }

    /**
     * 转换时区
     *
     * @param date           日期 YYYY-mm-DD HH:mm:ss
     * @param targetTimeZone 目标时区 Asia/Shanghai
     * @return Date
     * @throws ParseException 解析日期异常
     * @deprecated This method is deprecated.
     */
    @Deprecated
    public static Date convertTimeZone(Date date, String targetTimeZone) throws ParseException {
        return convertTimeZone(date, DateUtils.YMD_HMS_PATTERN, targetTimeZone);
    }

    /**
     * @param date       日期 YYYY-mm-DD HH:mm:ss
     * @param dateFormat 日期格式 "YYYY-mm-DD HH:mm:ss"
     * @return Date
     * @throws ParseException 解析日期异常
     * @deprecated This method is deprecated.
     */
    @Deprecated
    public static Date convertUtc(Date date, String dateFormat) throws ParseException {
        return convertTimeZone(date, dateFormat, UTC);
    }

    /**
     * @param date 日期 YYYY-mm-DD HH:mm:ss
     * @return Date
     * @throws ParseException 解析日期异常
     * @deprecated This method is deprecated.
     */
    @Deprecated
    public static Date convertUtc(Date date) throws ParseException {
        return convertTimeZone(date, DateUtils.YMD_HMS_PATTERN, UTC);
    }

}
