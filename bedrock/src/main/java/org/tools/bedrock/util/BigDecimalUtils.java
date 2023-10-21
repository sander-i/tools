package org.tools.bedrock.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sander
 * @version 1.0
 * @date 2023/04/08 20:37
 * @description BigDecimal 工具
 * @scope all
 */
public class BigDecimalUtils {

    /**
     * 加法
     *
     * @param v1 值1
     * @param v2 值2
     * @return 和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 减法
     *
     * @param v1 值1
     * @param v2 值2
     * @return 差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 乘法
     *
     * @param v1 值1
     * @param v2 值2
     * @return 积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 除法
     *
     * @param v1 值1
     * @param v2 值2
     * @return 商
     */
    public static double divide(double v1, double v2) {
        if (v2 <= 0) {
            throw new IllegalArgumentException("除法非法");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, 2, RoundingMode.HALF_UP).doubleValue();
    }

}
