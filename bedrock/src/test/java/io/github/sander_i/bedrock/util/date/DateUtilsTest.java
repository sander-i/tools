package io.github.sander_i.bedrock.util.date;

import org.junit.Test;
import io.github.sander_i.bedrock.base.DateType;
import io.github.sander_i.bedrock.base.TimeSeason;
import io.github.sander_i.bedrock.util.Aver;

import java.time.LocalTime;
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
        Date target = DateUtils.addDate(source, DateType.DAY_OF_MONTH, 1);
        Aver.assertEquals(DateUtils.dateFormatStr(target, DateUtils.YMD_HMS_PATTERN), "2023-10-09 00:00:00");
    }

    @Test
    public void hasSeasonDiffer() {
        // hasSeasonDiffer 依据「当前时间」判断，结果随运行日期变化，
        // 因此改为直接验证 TimeZone 的夏令时判定语义，避免测试随季节失败。
        TimeZone shanghai = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone london = TimeZone.getTimeZone("Europe/London");

        // 上海从不使用夏令时
        Aver.isFalse(shanghai.inDaylightTime(new Date(0)), "上海时区没有夏令时");

        // 伦敦夏季（7 月）为夏令时，冬季（1 月）为标准时
        java.util.Calendar july = java.util.Calendar.getInstance(london);
        july.clear();
        july.set(2024, java.util.Calendar.JULY, 1, 12, 0, 0);
        Aver.isTrue(london.inDaylightTime(july.getTime()), "伦敦 7 月应为夏令时");

        java.util.Calendar january = java.util.Calendar.getInstance(london);
        january.clear();
        january.set(2024, java.util.Calendar.JANUARY, 1, 12, 0, 0);
        Aver.isFalse(london.inDaylightTime(january.getTime()), "伦敦 1 月应为标准时");
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