package org.tools.bedrock.util.date;

import org.junit.Test;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class DateUtilsTest {

    @Test
    public void format() {
    }

    @Test
    public void strDateFormat() {
    }

    @Test
    public void testStrDateFormat() {
    }

    @Test
    public void dateFormatStr() {
    }

    @Test
    public void testDateFormatStr() {
    }

    @Test
    public void subBirthday() {
    }

    @Test
    public void getUTCDate() {
    }

    @Test
    public void getUTCDateStr() {
    }

    @Test
    public void sameWeek() {
        Date source = DateUtils.strDateFormat("2022-10-08 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-10 00:00:00");
        boolean b = DateUtils.sameWeek(source, target);
        System.out.println(b);
    }

    @Test
    public void sameMonth() {
        Date source = DateUtils.strDateFormat("2022-10-01 00:00:00");
        Date target = DateUtils.strDateFormat("2023-10-31 00:00:00");
        boolean b = DateUtils.sameMonth(source, target);
        System.out.println(b);
    }

    @Test
    public void timeDiff() {
    }

    @Test
    public void dateDiff() {
    }

    @Test
    public void dateTimeDiff() {
    }

    @Test
    public void addDate() {
    }

    @Test
    public void hasSeasonDiffer() {
    }

    @Test
    public void getSeasonDiffer() {
    }

    @Test
    public void testTimeDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 00:00:00 333");
        Date target = DateUtils.strDateFormat("2023-10-08 06:30:30 222");
        LocalTime localTime = DateUtils.timeDiff(source, target);
        System.out.println(localTime);
    }

    @Test
    public void testDateDiff() {
        Date source = DateUtils.strDateFormat("2023-10-08 16:00:00 333");
        Date target = DateUtils.strDateFormat("2023-10-18 06:30:30 222");
        Long diff = DateUtils.dateDiff(source, target);
        System.out.println(diff);
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
        System.out.println(overlap);
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
        System.out.println(overlap);
    }
}