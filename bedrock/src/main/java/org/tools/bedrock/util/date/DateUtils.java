package org.tools.bedrock.util.date;

import lombok.extern.slf4j.Slf4j;

import org.tools.bedrock.base.TimeSeason;
import org.tools.bedrock.util.BaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 日期工具：格式化、解析、同周/同月/同年判断与生日推算。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
@Slf4j
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
     * 时:分
     */
    public static final String HM_PATTERN = "HH:mm";

    /**
     * 时
     */
    public static final String H_PATTERN = "HH";

    /**
     * 按指定模式格式化日期
     *
     * @param date    日期
     * @param pattern 格式模式
     * @return 格式化后的字符串，date 为空返回 null
     */
    public static String format(Date date, String pattern) {
        if (BaseUtils.isEmpty(date)) {
            return null;
        }
        return new ThreadSafeSimpleDateFormat(pattern).format(date);
    }

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
                            log.error("string to date (auto match format)", hms);
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
            log.error("string to date", e);
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
                            log.error("date to string format", hms);
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
     * @param date    日期
     * @param pattern 日期格式
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
    public static Date getUTCDate() {
        return TimeZoneUtils.covertTimeZoneUTC(new Date(), YMD_HMS_PATTERN, TimeZone.getDefault().getID());
    }

    /**
     * 获取当前的UTC时间
     *
     * @return Date
     */
    public static String getUTCDateStr() {
        return TimeZoneUtils.covertTimeZoneUTCStr(new Date(), YMD_HMS_PATTERN, TimeZone.getDefault().getID());
    }

    /**
     * 判断两个日期 calendar 是否相同
     *
     * @param source   源日期
     * @param calendar Calendar
     * @param target   目标日期
     * @return calendar 是否相同
     */
    public static boolean same(Date source, int calendar, Date target) {
        Calendar sourceCalendar = Calendar.getInstance();
        sourceCalendar.setTime(source);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(target);
        return sourceCalendar.get(calendar) == targetCalendar.get(calendar);
    }

    /**
     * 判断是否为单周周末
     *
     * @param date 日期
     * @return 是否为单周周末
     */
    public static boolean singleWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SUNDAY;
    }

    /**
     * 判断是否为双周周末
     *
     * @param date 日期
     * @return 是否为双周周末
     */
    public static boolean weekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
    }

    /**
     * 判断两个日期是否在同一周内
     *
     * @param source 源日期
     * @param target 目标日期
     * @return 日期是否在同一周
     */
    public static boolean sameWeek(Date source, Date target) {
        return same(source, Calendar.WEEK_OF_YEAR, target);
    }

    /**
     * 判断两个日期是否在同一月内
     *
     * @param source 源日期
     * @param target 目标日期
     * @return 日期是否在同一月
     */
    public static boolean sameMonth(Date source, Date target) {
        return same(source, Calendar.MONTH, target);
    }

    /**
     * 判断两个日期是否在同一月内
     *
     * @param source 源日期
     * @param target 目标日期
     * @return 日期是否在同一月
     */
    public static boolean sameYear(Date source, Date target) {
        return same(source, Calendar.YEAR, target);
    }

    /**
     * 计算两个日期时间差（2024-01-11 01:26:45, 2024-01-11 11:30:15 = 10h3m30s）
     * 如果存在天数差异请调用
     *
     * @param source 源时间
     * @param target 目标时间
     * @return 时间差
     */
    public static LocalTime timeDiff(Date source, Date target) {
        LocalDateTime sourceDateTime = LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        LocalDateTime targetDateTime = LocalDateTime.ofInstant(target.toInstant(), ZoneId.systemDefault());

        LocalDate localDate = sourceDateTime.toLocalDate();
        LocalDate targetDate = targetDateTime.toLocalDate();
        if (!localDate.equals(targetDate)) {
            return LocalTime.now();
        }

        LocalTime sourceTime = sourceDateTime.toLocalTime();
        LocalTime targetTime = targetDateTime.toLocalTime();

        Duration duration = Duration.between(sourceTime, targetTime);
        return DateConvert.toLocalTime(duration);
    }

    /**
     * 计算两个日期天数差 (2024-01-11 00:00:00, 2024-01-13 23:59:59 = 2d)
     *
     * @param source 源日期
     * @param target 目标日期
     * @return 天数差
     */
    public static Long dateDiff(Date source, Date target) {
        return ChronoUnit.DAYS.between(
                DateConvert.toLocalDate(source),
                DateConvert.toLocalDate(target)
        );
    }

    /**
     * 根据类型对日期进行加|减处理
     *
     * @param source 源日期
     * @param type   Calendar 日期字段常量（如 {@link Calendar#DAY_OF_MONTH}、{@link Calendar#HOUR_OF_DAY}）
     * @param val    值
     * @return 加|减后的日期
     */
    public static Date addDate(Date source, int type, int val) {
        Calendar calendar = DateConvert.toCalendar(source);
        calendar.add(type, val);
        return calendar.getTime();
    }

    /**
     * 判断时区是否存在夏令时、冬令时
     *
     * @param timeZone 时区
     * @return 是否存在夏令时、冬令时
     */
    public static boolean hasSeasonDiffer(TimeZone timeZone) {
        return timeZone.inDaylightTime(new Date());
    }

    /**
     * 获取时区当前时令：夏令时生效返回 {@link TimeSeason#SUMMER_TIME}，
     * 否则返回 {@link TimeSeason#STANDARD_TIME}；
     * {@link TimeSeason#WINTER_TIME} 预留给冬令时场景，本方法不返回。
     *
     * @param timeZone 时区
     * @return 当前时令
     */
    public static TimeSeason getSeasonDiffer(TimeZone timeZone) {
        return timeZone.inDaylightTime(new Date()) ? TimeSeason.SUMMER_TIME : TimeSeason.STANDARD_TIME;
    }

    /**
     * 判断两个时间区间是否存在重叠部分
     *
     * @param source 源日期
     * @param target 目标日期
     * @return 是否存在交集
     */
    public static boolean overlap(DateRange source, DateRange target) {
        return (source.getStartDate().before(target.getEndDate())) &&
                (source.getEndDate().after(target.getStartDate()));
    }

    /**
     * 判断两个时间区间是否存在重叠部分
     *
     * @param source 源日期
     * @param target 目标日期
     * @param type   日期类型
     * @return 是否存在交集
     */
    public static boolean overlap(DateRange source, DateRange target, DateRangeType type) {
        switch (type) {
            case LOCAL_DATE_TIME:
                return source.getStartLocalDateTime().isBefore(target.getEndLocalDateTime()) &&
                        source.getEndLocalDateTime().isAfter(target.getStartLocalDateTime());
            case LOCAL_DATE:
                return source.getStartLocalDate().isBefore(target.getEndLocalDate()) &&
                        source.getEndLocalDate().isAfter(target.getStartLocalDate());
            case LOCAL_TIME:
                return (source.getStartLocalTime().isBefore(target.getEndLocalTime())) &&
                        (source.getEndLocalTime().isAfter(target.getStartLocalTime()));
            case TIMESTAMP:
                return (source.getStartTimestamp().before(target.getEndTimestamp())) &&
                        (source.getEndTimestamp().after(target.getStartTimestamp()));
            case DATE:
            default:
                return overlap(source, target);
        }
    }

    /**
     * 获取两个时间区间存在|不存在重叠部分日期
     *
     * @param source 源日期
     * @param target 目标日期
     * @param repeat 重复 default true
     * @return 是否存在交集
     */
    public static List<String> getOverlap(DateRange source, DateRange target, boolean repeat) {
        Calendar calendarStart1 = DateConvert.toCalendar(source.getStartDate());

        Calendar calendarEnd1 = DateConvert.toCalendar(source.getEndDate());

        Calendar calendarStart2 = DateConvert.toCalendar(target.getStartDate());

        Calendar calendarEnd2 = DateConvert.toCalendar(target.getEndDate());

        return repeat ?
                getIntersectionDates(calendarStart1, calendarEnd1, calendarStart2, calendarEnd2) :
                getDifferenceDates(calendarStart1, calendarEnd1, calendarStart2, calendarEnd2);
    }

    /**
     * 获取两个时间区间存在重叠部分日期
     *
     * @param start1 时间区间的开始时间
     * @param end1   时间区间的结束时间
     * @param start2 时间区间的开始时间
     * @param end2   时间区间的结束时间
     * @return 存在重叠部分日期
     */
    private static List<String> getIntersectionDates(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
        List<String> result = new ArrayList<>();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return result;
        }

        long startMillis1 = start1.getTimeInMillis();
        long endMillis1 = end1.getTimeInMillis();
        long startMillis2 = start2.getTimeInMillis();
        long endMillis2 = end2.getTimeInMillis();

        long millis = Math.max(startMillis1, startMillis2);

        long min = Math.min(endMillis1, endMillis2);

        for (; millis <= min; millis += 86400000L) {
            Calendar currentDay = Calendar.getInstance();
            currentDay.setTimeInMillis(millis);

            result.add(new SimpleDateFormat(Y_M_D_PATTERN).format(currentDay.getTime()));
        }

        return result;
    }

    /**
     * 获取两个时间区间不存在重叠部分日期
     *
     * @param start1 时间区间的开始时间
     * @param end1   时间区间的结束时间
     * @param start2 时间区间的开始时间
     * @param end2   时间区间的结束时间
     * @return 不存在重叠部分日期
     */
    private static List<String> getDifferenceDates(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
        List<String> result = new ArrayList<>();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return result;
        }

        long startMillis1 = start1.getTimeInMillis();
        long endMillis1 = end1.getTimeInMillis();
        long startMillis2 = start2.getTimeInMillis();
        long endMillis2 = end2.getTimeInMillis();

        long minStart = Math.min(startMillis1, startMillis2);
        long maxEnd = Math.max(endMillis1, endMillis2);

        long maxStart = Math.max(startMillis1, startMillis2);
        long minEnd = Math.min(endMillis1, endMillis2);
        long millis = minStart;

        for (; millis <= maxEnd; millis += 86400000L) {
            if (millis >= maxStart && millis <= minEnd) {
                continue;
            }
            Calendar currentDay = Calendar.getInstance();
            currentDay.setTimeInMillis(millis);

            result.add(new SimpleDateFormat(Y_M_D_PATTERN).format(currentDay.getTime()));
        }

        return result;
    }

    /**
     * 生成开始日期到结束日期之间的日期列表
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期列表
     */
    public static List<Date> getDateRange(Date start, Date end) {
        if (BaseUtils.isNull(start, end)) {
            return Collections.emptyList();
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("开始日期不能大于结束日期");
        }
        if (start.equals(end)) {
            return Collections.singletonList(start);
        }

        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);

        while (!calendar.getTime().after(end)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        return dates;
    }
}
