package org.tools.bedrock.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author sander
 * @version 1.0
 * @date 2024/03/02 20:35
 * @description 避免传参魔法数字，定义枚举类型
 * @scope 作用域
 */
@Getter
@RequiredArgsConstructor
public enum DateType {

    /**
     * 闰年
     */
    LEAP_YEAR(0),
    /**
     * 平年
     */
    NON_LEAP_YEAR(1),
    /**
     * 年
     */
    YEAR(1),
    /**
     * 月
     */
    MONTH(2),
    /**
     * 几周(年)
     */
    WEEK_OF_YEAR(3),
    /**
     * 几周(月)
     */
    WEEK_OF_MONTH(4),
    /**
     * 日
     */
    DATE(5),
    /**
     * 日(月)
     */
    DAY_OF_MONTH(5),
    /**
     * 日(年)
     */
    DAY_OF_YEAR(6),
    /**
     * 日(周)
     */
    DAY_OF_WEEK(7),
    /**
     * 日(周-月)
     */
    DAY_OF_WEEK_IN_MONTH(8),
    /**
     * 上午/下午
     */
    AM_PM(9),
    /**
     * 时(12小时制)
     */
    HOUR(10),
    /**
     * 时(24小时制)
     */
    HOUR_OF_DAY(11),
    /**
     * 分
     */
    MINUTE(12),
    /**
     * 秒
     */
    SECOND(13),
    /**
     * 毫秒
     */
    MILLISECOND(14);

    private final int value;

}
