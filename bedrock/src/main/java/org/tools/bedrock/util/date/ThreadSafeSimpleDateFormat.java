package org.tools.bedrock.util.date;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 线程安全 SimpleDateFormat：ThreadLocal 持有实例，format/parse 并发安全。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
public class ThreadSafeSimpleDateFormat extends ThreadSafeDateFormat {
    private static final long serialVersionUID = -6344863844634215860L;

    private ThreadLocal<DateFormat> threadLocal = null;

    public ThreadSafeSimpleDateFormat(String pattern) {
        threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
    }

    public ThreadSafeSimpleDateFormat(String pattern, Locale locale) {
        threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern, locale));
    }

    public ThreadSafeSimpleDateFormat(String pattern, Locale locale, final TimeZone zone) {
        threadLocal = ThreadLocal.withInitial(() -> {
            SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
            format.setTimeZone(zone);
            return format;
        });
    }

    /** 格式化（ThreadLocal 代理实现，线程安全） */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return threadLocal.get().format(date, toAppendTo, fieldPosition);
    }

    /** 解析（ThreadLocal 代理实现，线程安全） */
    @Override
    public Date parse(String source, ParsePosition pos) {
        return threadLocal.get().parse(source, pos);
    }

    /** 委托设置 Calendar */
    @Override
    public void setCalendar(Calendar newCalendar) {
        threadLocal.get().setCalendar(newCalendar);
    }

    /** 委托读取 Calendar */
    @Override
    public Calendar getCalendar() {
        return threadLocal.get().getCalendar();
    }

    /** 委托设置数字格式 */
    @Override
    public void setNumberFormat(NumberFormat newNumberFormat) {
        threadLocal.get().setNumberFormat(newNumberFormat);
    }

    /** 委托读取数字格式 */
    @Override
    public NumberFormat getNumberFormat() {
        return threadLocal.get().getNumberFormat();
    }

    /** 委托设置时区 */
    @Override
    public void setTimeZone(TimeZone zone) {
        threadLocal.get().setTimeZone(zone);
    }

    /** 委托读取时区 */
    @Override
    public TimeZone getTimeZone() {
        return threadLocal.get().getTimeZone();
    }

    /** 委托设置宽松解析开关 */
    @Override
    public void setLenient(boolean lenient) {
        threadLocal.get().setLenient(lenient);
    }

    /** 委托读取宽松解析开关 */
    @Override
    public boolean isLenient() {
        return threadLocal.get().isLenient();
    }

    /** 委托计算哈希值 */
    @Override
    public int hashCode() {
        return threadLocal.get().hashCode();
    }

    /** 委托判断相等 */
    public boolean equals(Object obj) {
        return threadLocal.get().equals(obj);
    }

    /** 委托克隆内部格式实例 */
    public Object clone() {
        return threadLocal.get().clone();
    }
}
