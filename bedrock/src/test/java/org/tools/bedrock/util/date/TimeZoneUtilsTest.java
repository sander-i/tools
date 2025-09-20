package org.tools.bedrock.util.date;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;


public class TimeZoneUtilsTest {

    @Test
    public void testTimeZoneUtilsSmoke() {
        // 冒烟测试：验证 TimeZoneUtils 类的基本功能

        String sourceDate = "2023-12-25 15:30:45";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String sourceTimeZone = "GMT+8";
        String targetTimeZone = "GMT";

        // 测试字符串到字符串的时区转换
        String resultStr = TimeZoneUtils.covertTimeZoneStr(sourceDate, pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("字符串时区转换结果不应为null", resultStr);

        // 测试字符串到Date的时区转换
        Date resultDate = TimeZoneUtils.covertTimeZone(sourceDate, pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("Date时区转换结果不应为null", resultDate);

        // 测试Date到字符串的时区转换
        String resultFromDateString = TimeZoneUtils.covertTimeZoneStr(new Date(), pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("Date到字符串时区转换结果不应为null", resultFromDateString);

        // 测试Date到Date的时区转换
        Date resultFromDate = TimeZoneUtils.covertTimeZone(new Date(), pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("Date到Date时区转换结果不应为null", resultFromDate);

        // 测试UTC相关转换方法
        String utcStrResult = TimeZoneUtils.covertTimeZoneUTCStr(sourceDate, pattern, sourceTimeZone);
        assertNotNull("UTC字符串转换结果不应为null", utcStrResult);

        Date utcDateResult = TimeZoneUtils.covertTimeZoneUTC(sourceDate, pattern, sourceTimeZone);
        assertNotNull("UTC日期转换结果不应为null", utcDateResult);

        System.out.println("TimeZoneUtils冒烟测试通过：所有基本功能正常工作");
    }

    // 针对各个具体方法的简单测试
    @Test
    public void testCovertTimeZoneStr() {
        String result = TimeZoneUtils.covertTimeZoneStr("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneStrWithDate() {
        String result = TimeZoneUtils.covertTimeZoneStr(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZone() {
        Date result = TimeZoneUtils.covertTimeZone("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneWithDate() {
        Date result = TimeZoneUtils.covertTimeZone(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneUTCStr() {
        String result = TimeZoneUtils.covertTimeZoneUTCStr("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneUTCStrWithDate() {
        String result = TimeZoneUtils.covertTimeZoneUTCStr(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneUTC() {
        Date result = TimeZoneUtils.covertTimeZoneUTC("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testCovertTimeZoneUTCWithDate() {
        Date result = TimeZoneUtils.covertTimeZoneUTC(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }
}