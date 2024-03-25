package org.tools.bedrock.util.date;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/10 17:16
 * @description 描述
 * @scope 作用域
 */
public class DateConvert {

    private DateConvert() {
    }

    /**
     * 转换Duration为LocalTime
     *
     * @param duration 期间
     * @return LocalTime
     */
    public static LocalTime toLocalTime(Duration duration) {
        return LocalTime.of(
                (int) duration.toHours(),
                (int) (duration.toMinutes() - (duration.toHours() * 60)),
                (int) (duration.getSeconds() - (duration.toMinutes() * 60)),
                (int) duration.get(ChronoUnit.NANOS)
        );
    }

    /**
     * 转换Duration为LocalTime，忽略纳秒
     *
     * @param duration 期间
     * @return LocalTime
     */
    public static LocalTime toLocalTimeNoNanoTime(Duration duration) {
        return LocalTime.ofSecondOfDay(duration.getSeconds());
    }

    /**
     * 转换Date为LocalDate
     *
     * @param date 日期
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return LocalDate.ofEpochDay(date.getTime() / 1000 / 60 / 60 / 24);
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar[] toCalendar(Date... dates) {
        Calendar[] calendars = new Calendar[dates.length];

        for (int i = 0; i < dates.length; i++) {
            calendars[i] = toCalendar(dates[i]);
        }

        return calendars;
    }

}
