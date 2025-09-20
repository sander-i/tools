package org.tools.bedrock.util;

import org.tools.bedrock.constant.Constants;
import org.tools.bedrock.constant.v2.SpecialSymbolConst;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sander
 * @version 1.0
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {
    }

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
        return b1.multiply(b2).setScale(Constants.TWO_I, RoundingMode.HALF_UP).doubleValue();
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
        return b1.divide(b2, Constants.TWO_I, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * crawler获取日元字符串转BigDecimal
     *
     * @param strVal 字符串日元
     * @return BigDecimal
     */
    public static BigDecimal strValToBigDecimal(String strVal) {
        return new BigDecimal(strVal.replace(",", ""));
    }

    /**
     * crawler获取日元字符串转BigDecimal
     *
     * @param strVal           字符串日元
     * @param handlingDecimals 处理小数位
     * @return BigDecimal
     */
    public static BigDecimal strValToBigDecimal(String strVal, boolean handlingDecimals) {
        return strValToBigDecimal(strVal, handlingDecimals, Constants.TWO_I);
    }

    /**
     * crawler获取日元字符串转BigDecimal
     * 本身如果是正确的小数位符号直接由BigDecimal处理
     *
     * @param strVal           字符串日元
     * @param handlingDecimals 处理小数位
     * @param decimalPlaces    小数位数
     * @return BigDecimal
     */
    public static BigDecimal strValToBigDecimal(String strVal, boolean handlingDecimals, int decimalPlaces) {
        // 正则校验位数分隔符是否符合规范
        if (strVal.matches("^(,\\d{3})*([,.]\\d+)?$"))
            throw new IllegalArgumentException("当前金额格式不符合规范！");

        if (!handlingDecimals) return strValToBigDecimal(strVal);

        int semicolon = strVal.lastIndexOf(SpecialSymbolConst.SEMICOLON);

        String strBefore = strVal.substring(Constants.ZERO_I, semicolon);
        String strAfter = strVal.substring(semicolon)
                .replace(SpecialSymbolConst.SEMICOLON,
                        SpecialSymbolConst.POINT);

        // 小数位数
        return (strVal.length() - semicolon) - Constants.ONE_I <= decimalPlaces ?
                strValToBigDecimal(strBefore + strAfter) : strValToBigDecimal(strVal);
    }

    /**
     * 检查BigDecimal是否为null
     *
     * @param value 待检查的BigDecimal值
     * @return 如果为null则返回true，否则返回false
     */
    public static boolean isNull(BigDecimal value) {
        return value == null;
    }

    /**
     * 检查BigDecimal是否为零
     *
     * @param value 待检查的BigDecimal值
     * @return 如果为零则返回true，否则返回false
     */
    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }

}
