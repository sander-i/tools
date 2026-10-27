package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时令枚举：标准时/夏令时/冬令时。
 *
 * <p>夏令时判定见 {@code DateUtils#getSeasonDiffer}（按当前时刻是否处于
 * 夏令时返回 SUMMER_TIME/STANDARD_TIME）；WINTER_TIME 预留给南半球式
 * 冬令时场景，现有工具方法不会返回。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.4
 */
@Getter
@AllArgsConstructor
public enum TimeSeason {
    /**
     * 标准时
     */
    STANDARD_TIME(0),

    /**
     * 夏令时
     */
    SUMMER_TIME(1),

    /**
     * 冬令时
     */
    WINTER_TIME(2);

    private final int value;
}
