package org.tools.bedrock.util.date;

import org.junit.Test;
import org.tools.bedrock.base.TimeSeason;
import org.tools.bedrock.util.Aver;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtilsTest {

    @Test
    public void format() {
    }

    @Test
    public void strDateFormat() {
        Date date = DateUtils.strDateFormat("2023-10-08 00:00:00");
        Aver.notNull(date, "字符串转日期失败");
    }

    @Test
    public void testStrDateFormat() {
        Date date = DateUtils.strDateFormat("2023-10-08 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Aver.notNull(date, "字符串转日期失败");
    }

    @Test
    public void dateFormatStr() {
        String dateStr = DateUtils.dateFormatStr(new Date());
        Aver.notNull(dateStr, "日期转字符串格式化失败");
    }

    @Test
    public void testDateFormatStr() {
        String dateStr = DateUtils.dateFormatStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        Aver.notNull(dateStr, "日期转字符串格式化失败");
    }

    @Test
    public void subBirthday() {
        Date date = DateUtils.subBirthday("432114202011066001");
        Aver.assertEquals(date, DateUtils.strDateFormat("2020-11-06"));
    }

    @Test
    public void getUTCDate() {
        Date date = DateUtils.getUTCDate();
        Aver.notNull(date, "获取UTC时间失败");
    }

    @Test
    public void getUTCDateStr() {
        String dateStr = DateUtils.getUTCDateStr();
        Aver.notNull(dateStr, "获取UTC时间失败");
    }

    @Test
    public void sameWeek() {
        Date source = DateUtils.strDateFormat("2022-10-08 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-10 00:00:00");
        boolean b = DateUtils.sameWeek(source, target);
        Aver.isTrue(b, "2022-10-08 00:00:00 和 2023-10-10 00:00:00 同周验证失败");
    }

    @Test
    public void sameMonth() {
        Date source = DateUtils.strDateFormat("2022-10-01 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-31 00:00:00");
        boolean b = DateUtils.sameMonth(source, target);
        Aver.isTrue(b, "2022-10-01 00:00:00 和 2023-10-31 00:00:00 同月验证失败");
    }

    @Test
    public void timeDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-08 06:30:30");
        LocalTime localTime = DateUtils.timeDiff(source, target);
        Aver.assertEquals(localTime.getHour(), 6);
    }

    @Test
    public void dateDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-08 06:30:30");
        Long diff = DateUtils.dateDiff(source, target);
        Aver.assertEquals(diff, 0L);
    }

    @Test
    public void dateTimeDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-08 06:30:30");
        LocalTime localTime = DateUtils.timeDiff(source, target);
        Aver.assertEquals(localTime.getHour(), 6);
    }

    @Test
    public void addDate() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00");
        Date target = DateUtils.addDate(source, Calendar.DAY_OF_MONTH, 1);
        Aver.assertEquals(DateUtils.dateFormatStr(target, DateUtils.YMD_HMS_PATTERN), "2023-10-09 00:00:00");
    }

    @Test
    public void hasSeasonDiffer() {
        // 上海不实行夏令时，任何时刻都应返回 false
        Aver.isFalse(DateUtils.hasSeasonDiffer(TimeZone.getTimeZone("Asia/Shanghai")), "上海时区没有夏令时");

        // 伦敦实行夏令时；hasSeasonDiffer 判定的是"当前时刻"是否处于夏令时，
        // 结果随运行日期变化（10 月最后一个周日后至次年 3 月为 false），与 JDK 判定一致即可
        TimeZone london = TimeZone.getTimeZone("Europe/London");
        Aver.isTrue(london.useDaylightTime(), "伦敦时区实行夏令时");
        Aver.equals(london.inDaylightTime(new Date()), DateUtils.hasSeasonDiffer(london), "伦敦时区夏令时判定应与 JDK 一致");
    }

    @Test
    public void getSeasonDiffer() {
        TimeSeason seasonDiffer = DateUtils.getSeasonDiffer(TimeZone.getTimeZone("Asia/Shanghai"));
        Aver.assertEquals(seasonDiffer, TimeSeason.STANDARD_TIME);
    }

    @Test
    public void testTimeDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00 333");
        Date target = DateUtils.strDateFormat("2023-10-08 06:30:30 222");
        LocalTime localTime = DateUtils.timeDiff(source, target);
        Aver.assertEquals(localTime.getHour(), 6);
    }

    @Test
    public void testDateDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 16:00:00 333");
        Date target = DateUtils.strDateFormat("2023-10-18 06:30:30 222");
        Long diff = DateUtils.dateDiff(source, target);
        Aver.assertEquals(diff, 9L);
    }

    @Test
    public void overlap() {
        DateRange source = new DateRange();
        source.setStartDate(DateUtils.strDateFormat("2023-10-01 00:00:00"));
        source.setEndDate(DateUtils.strDateFormat("2023-10-31 23:59:59"));
        DateRange target = new DateRange();
        target.setStartDate(DateUtils.strDateFormat("2023-09-01 00:00:00"));
        target.setEndDate(DateUtils.strDateFormat("2023-09-30 23:59:59"));

        boolean overlap = DateUtils.overlap(source, target);
        Aver.isFalse(overlap, "2023-10-01 00:00:00 和 2023-10-31 23:59:59 不重叠");
    }

    @Test
    public void testOverlap() {
        DateRange source = new DateRange();
        source.setStartDate(DateUtils.strDateFormat("2023-10-05 00:00:00"));
        source.setEndDate(DateUtils.strDateFormat("2023-10-10 23:59:59"));
        DateRange target = new DateRange();
        target.setStartDate(DateUtils.strDateFormat("2023-10-01 00:00:00"));
        target.setEndDate(DateUtils.strDateFormat("2023-10-07 23:59:59"));

        List<String> overlap = DateUtils.getOverlap(source, target, false);
        Aver.assertEquals(overlap.size(), 7);
    }
}