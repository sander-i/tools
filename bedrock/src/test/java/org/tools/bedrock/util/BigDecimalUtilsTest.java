package org.tools.bedrock.util;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class BigDecimalUtilsTest extends TestCase {

    public void testAdd() {
        // 测试基本加法运算
        double a = 10.5;
        double b = 20.3;
        double result = BigDecimalUtils.add(a, b);
        assertEquals(30.8, result, 0.001);
    }

    public void testSubtract() {
        // 测试基本减法运算
        double a = 30.5;
        double b = 10.2;
        double result = BigDecimalUtils.subtract(a, b);
        assertEquals(20.3, result, 0.001);
    }

    public void testMultiply() {
        // 测试基本乘法运算
        double a = 5.5;
        double b = 2.0;
        double result = BigDecimalUtils.multiply(a, b);
        assertEquals(11.0, result, 0.001);
    }

    public void testDivide() {
        // 测试基本除法运算
        double a = 10.0;
        double b = 2.0;
        double result = BigDecimalUtils.divide(a, b);
        assertEquals(5.0, result, 0.001);
    }

    public void testIsNullWithNullValue() {
        // 测试null值检查
        assertTrue(BigDecimalUtils.isNull(null));
    }

    public void testIsNullWithNonNullValue() {
        // 测试非null值检查
        BigDecimal value = new BigDecimal("0");
        assertFalse(BigDecimalUtils.isNull(value));
    }

    public void testIsZero() {
        // 测试零值检查
        BigDecimal zero = new BigDecimal("0.00");
        assertTrue(BigDecimalUtils.isZero(zero));

        BigDecimal nonZero = new BigDecimal("1.00");
        assertFalse(BigDecimalUtils.isZero(nonZero));

        // 测试null值
        assertFalse(BigDecimalUtils.isZero(null));
    }
}