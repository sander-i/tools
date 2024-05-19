package org.tools.bedrock.util.date;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author sander
 * @version 1.0
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

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return threadLocal.get().format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return threadLocal.get().parse(source, pos);
    }

    @Override
    public void setCalendar(Calendar newCalendar) {
        threadLocal.get().setCalendar(newCalendar);
    }

    @Override
    public Calendar getCalendar() {
        return threadLocal.get().getCalendar();
    }

    @Override
    public void setNumberFormat(NumberFormat newNumberFormat) {
        threadLocal.get().setNumberFormat(newNumberFormat);
    }

    @Override
    public NumberFormat getNumberFormat() {
        return threadLocal.get().getNumberFormat();
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        threadLocal.get().setTimeZone(zone);
    }

    @Override
    public TimeZone getTimeZone() {
        return threadLocal.get().getTimeZone();
    }

    @Override
    public void setLenient(boolean lenient) {
        threadLocal.get().setLenient(lenient);
    }

    @Override
    public boolean isLenient() {
        return threadLocal.get().isLenient();
    }

    @Override
    public int hashCode() {
        return threadLocal.get().hashCode();
    }

    public boolean equals(Object obj) {
        return threadLocal.get().equals(obj);
    }

    public Object clone() {
        return threadLocal.get().clone();
    }

}
