package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sander
 * @version 1.0
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
