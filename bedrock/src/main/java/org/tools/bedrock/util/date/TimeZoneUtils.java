package org.tools.bedrock.util.date;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author sander
 * @version 1.0
 */
@Slf4j
public class TimeZoneUtils {

    private TimeZoneUtils() {
    }

    /** 字符串日期时区转换，返回目标时区字符串 */
    public static String covertTimeZoneStr(String sourceDate, String pattern, String sourceTimeZone, String targetTimeZone) {
        ThreadSafeSimpleDateFormat format = new ThreadSafeSimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone(sourceTimeZone));
        Date utc;
        try {
            utc = format.parse(sourceDate);
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        format.setTimeZone(TimeZone.getTimeZone(targetTimeZone));
        return format.format(utc);
    }

    /** Date 时区转换，返回目标时区字符串 */
    public static String covertTimeZoneStr(Date source, String pattern, String sourceTimeZone, String targetTimeZone) {
        return covertTimeZoneStr(new SimpleDateFormat(pattern).format(source), pattern, sourceTimeZone, targetTimeZone);
    }

    /** 字符串日期时区转换，返回目标时区 Date */
    public static Date covertTimeZone(String sourceDate, String pattern, String sourceTimeZone, String targetTimeZone) {
        String targetDate = covertTimeZoneStr(sourceDate, pattern, sourceTimeZone, targetTimeZone);
        if (targetDate == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(targetDate);
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /** Date 时区转换，返回目标时区 Date */
    public static Date covertTimeZone(Date source, String pattern, String sourceTimeZone, String targetTimeZone) {
        return covertTimeZone(new SimpleDateFormat(pattern).format(source), pattern, sourceTimeZone, targetTimeZone);
    }

    /** 字符串日期转 UTC 字符串 */
    public static String covertTimeZoneUTCStr(String sourceDate, String pattern, String sourceTimeZone) {
        return covertTimeZoneStr(sourceDate, pattern, sourceTimeZone, "UTC");
    }

    /** Date 转 UTC 字符串 */
    public static String covertTimeZoneUTCStr(Date source, String pattern, String sourceTimeZone) {
        return covertTimeZoneStr(source, pattern, sourceTimeZone, "UTC");
    }

    /** 字符串日期转 UTC Date */
    public static Date covertTimeZoneUTC(String sourceDate, String pattern, String sourceTimeZone) {
        return covertTimeZone(sourceDate, pattern, sourceTimeZone, "UTC");
    }

    /** Date 转 UTC Date */
    public static Date covertTimeZoneUTC(Date source, String pattern, String sourceTimeZone) {
        return covertTimeZone(source, pattern, sourceTimeZone, "UTC");
    }

}
