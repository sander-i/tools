package org.tools.bedrock.util;

import org.junit.Test;
import org.junit.Assert;

import java.math.BigDecimal;

public class BigDecimalUtilsTest {

    @Test
    public void testAdd() {
        // 测试基本加法运算
        double a = 10.5;
        double b = 20.3;
        double result = BigDecimalUtils.add(a, b);
        Assert.assertEquals(30.8, result, 0.001);
    }

    @Test
    public void testSubtract() {
        // 测试基本减法运算
        double a = 30.5;
        double b = 10.2;
        double result = BigDecimalUtils.subtract(a, b);
        Assert.assertEquals(20.3, result, 0.001);
    }

    @Test
    public void testMultiply() {
        // 测试基本乘法运算
        double a = 5.5;
        double b = 2.0;
        double result = BigDecimalUtils.multiply(a, b);
        Assert.assertEquals(11.0, result, 0.001);
    }

    @Test
    public void testDivide() {
        // 测试基本除法运算
        double a = 10.0;
        double b = 2.0;
        double result = BigDecimalUtils.divide(a, b);
        Assert.assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testIsNullWithNullValue() {
        // 测试null值检查
        Assert.assertTrue(BigDecimalUtils.isNull(null));
    }

    @Test
    public void testIsNullWithNonNullValue() {
        // 测试非null值检查
        BigDecimal value = new BigDecimal("0");
        Assert.assertFalse(BigDecimalUtils.isNull(value));
    }

    @Test
    public void testIsZero() {
        // 测试零值检查
        BigDecimal zero = new BigDecimal("0.00");
        Assert.assertTrue(BigDecimalUtils.isZero(zero));

        BigDecimal nonZero = new BigDecimal("1.00");
        Assert.assertFalse(BigDecimalUtils.isZero(nonZero));

        // 测试null值
        Assert.assertFalse(BigDecimalUtils.isZero(null));
    }
}