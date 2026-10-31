package org.tools.bedrock.util.date;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TimeZoneUtilsTest 单元测试
 *
 * @author sander
 * @since 1.0.6.6
 */
@SuppressWarnings("deprecation")
public class TimeZoneUtilsTest {
    @Test
    public void testTimeZoneUtilsSmoke() {
        // 冒烟测试：验证 TimeZoneUtils 类的基本功能

        String sourceDate = "2023-12-25 15:30:45";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String sourceTimeZone = "GMT+8";
        String targetTimeZone = "GMT";

        // 测试字符串到字符串的时区转换
        String resultStr = TimeZoneUtils.convertTimeZoneStr(sourceDate, pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("字符串时区转换结果不应为null", resultStr);

        // 测试字符串到Date的时区转换
        Date resultDate = TimeZoneUtils.convertTimeZone(sourceDate, pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("Date时区转换结果不应为null", resultDate);

        // 测试Date到字符串的时区转换
        String resultFromDateString = TimeZoneUtils.convertTimeZoneStr(new Date(), pattern, sourceTimeZone,
                targetTimeZone);
        assertNotNull("Date到字符串时区转换结果不应为null", resultFromDateString);

        // 测试Date到Date的时区转换
        Date resultFromDate = TimeZoneUtils.convertTimeZone(new Date(), pattern, sourceTimeZone, targetTimeZone);
        assertNotNull("Date到Date时区转换结果不应为null", resultFromDate);

        // 测试UTC相关转换方法
        String utcStrResult = TimeZoneUtils.convertTimeZoneUTCStr(sourceDate, pattern, sourceTimeZone);
        assertNotNull("UTC字符串转换结果不应为null", utcStrResult);

        Date utcDateResult = TimeZoneUtils.convertTimeZoneUTC(sourceDate, pattern, sourceTimeZone);
        assertNotNull("UTC日期转换结果不应为null", utcDateResult);

        System.out.println("TimeZoneUtils冒烟测试通过：所有基本功能正常工作");
    }

    // 针对各个具体方法的简单测试
    @Test
    public void testConvertTimeZoneStr() {
        String result = TimeZoneUtils.convertTimeZoneStr("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneStrWithDate() {
        String result = TimeZoneUtils.convertTimeZoneStr(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZone() {
        Date result = TimeZoneUtils.convertTimeZone("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneWithDate() {
        Date result = TimeZoneUtils.convertTimeZone(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8", "GMT");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneUTCStr() {
        String result = TimeZoneUtils.convertTimeZoneUTCStr("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneUTCStrWithDate() {
        String result = TimeZoneUtils.convertTimeZoneUTCStr(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneUTC() {
        Date result = TimeZoneUtils.convertTimeZoneUTC("2023-12-25 12:00:00", "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    @Test
    public void testConvertTimeZoneUTCWithDate() {
        Date result = TimeZoneUtils.convertTimeZoneUTC(new Date(), "yyyy-MM-dd HH:mm:ss", "GMT+8");
        assertNotNull(result);
    }

    // 旧拼写方法（covertXxx）与正确拼写方法（convertXxx）的等价性验证
    @Test
    public void testConvertTimeZoneAliasEquivalence() throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String sourceDate = "2023-12-25 12:00:00";
        Date source = new SimpleDateFormat(pattern).parse(sourceDate);

        assertEquals(TimeZoneUtils.covertTimeZoneStr(sourceDate, pattern, "GMT+8", "GMT"),
                TimeZoneUtils.convertTimeZoneStr(sourceDate, pattern, "GMT+8", "GMT"));
        assertEquals(TimeZoneUtils.covertTimeZoneStr(source, pattern, "GMT+8", "GMT"),
                TimeZoneUtils.convertTimeZoneStr(source, pattern, "GMT+8", "GMT"));
        assertEquals(TimeZoneUtils.covertTimeZone(sourceDate, pattern, "GMT+8", "GMT"),
                TimeZoneUtils.convertTimeZone(sourceDate, pattern, "GMT+8", "GMT"));
        assertEquals(TimeZoneUtils.covertTimeZone(source, pattern, "GMT+8", "GMT"),
                TimeZoneUtils.convertTimeZone(source, pattern, "GMT+8", "GMT"));
    }

    @Test
    public void testConvertTimeZoneUTCAliasEquivalence() throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String sourceDate = "2023-12-25 12:00:00";
        Date source = new SimpleDateFormat(pattern).parse(sourceDate);

        assertEquals(TimeZoneUtils.covertTimeZoneUTCStr(sourceDate, pattern, "GMT+8"),
                TimeZoneUtils.convertTimeZoneUTCStr(sourceDate, pattern, "GMT+8"));
        assertEquals(TimeZoneUtils.covertTimeZoneUTCStr(source, pattern, "GMT+8"),
                TimeZoneUtils.convertTimeZoneUTCStr(source, pattern, "GMT+8"));
        assertEquals(TimeZoneUtils.covertTimeZoneUTC(sourceDate, pattern, "GMT+8"),
                TimeZoneUtils.convertTimeZoneUTC(sourceDate, pattern, "GMT+8"));
        assertEquals(TimeZoneUtils.covertTimeZoneUTC(source, pattern, "GMT+8"),
                TimeZoneUtils.convertTimeZoneUTC(source, pattern, "GMT+8"));
    }
}
