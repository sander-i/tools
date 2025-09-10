package org.tools.bedrock.util.date;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateConvertTest extends TestCase {

    public void testToLocalTime() {
        // 测试转换为LocalTime（包含纳秒）
        // 注意：DateConvert.toLocalTime() 实际接受 Duration 参数，不是 Date
        // 这里需要构造一个 Duration 来测试
        java.time.Duration duration = java.time.Duration.ofHours(2).plusMinutes(30).plusSeconds(45).plusNanos(123456789);
        java.time.LocalTime localTime = DateConvert.toLocalTime(duration);
        assertNotNull("LocalTime转换结果不应为null", localTime);
    }

    public void testToLocalTimeNoNanoTime() {
        // 测试转换为LocalTime（不包含纳秒）
        // 注意：DateConvert.toLocalTimeNoNanoTime() 实际接受 Duration 参数，不是 Date
        java.time.Duration duration = java.time.Duration.ofHours(2).plusMinutes(30).plusSeconds(45);
        java.time.LocalTime localTime = DateConvert.toLocalTimeNoNanoTime(duration);
        assertNotNull("LocalTime(无纳秒)转换结果不应为null", localTime);
    }

    public void testToLocalDate() {
        // 测试转换为LocalDate
        Date date = new Date();
        LocalDate localDate = DateConvert.toLocalDate(date);
        assertNotNull("LocalDate转换结果不应为null", localDate);
    }

    public void testToCalendar() {
        // 测试转换为Calendar
        Date date = new Date();
        Calendar calendar = DateConvert.toCalendar(date);
        assertNotNull("Calendar转换结果不应为null", calendar);
    }

    public void testTestToCalendar() {
        // 测试另一种Calendar转换方法（如果存在重载）
        Date date = new Date();
        Calendar calendar = DateConvert.toCalendar(date);
        assertNotNull("Calendar转换结果不应为null", calendar);
    }

    // 冒烟测试：验证所有基本转换功能
    public void testDateConvertSmoke() {
        Date date = new Date();
        java.time.Duration duration = java.time.Duration.ofHours(2).plusMinutes(30).plusSeconds(45).plusNanos(123456789);

        // 验证所有转换方法都能正常工作且不返回null
        assertNotNull("toLocalTime应返回有效值", DateConvert.toLocalTime(duration));
        assertNotNull("toLocalTimeNoNanoTime应返回有效值", DateConvert.toLocalTimeNoNanoTime(duration));
        assertNotNull("toLocalDate应返回有效值", DateConvert.toLocalDate(date));
        assertNotNull("toCalendar应返回有效值", DateConvert.toCalendar(date));

        System.out.println("DateConvert冒烟测试通过：所有基本转换功能正常");
    }
}