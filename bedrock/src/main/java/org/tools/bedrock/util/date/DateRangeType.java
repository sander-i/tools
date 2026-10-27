package org.tools.bedrock.util.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 日期区间类型枚举：DATE、LOCAL_DATE_TIME、LOCAL_DATE、LOCAL_TIME、TIMESTAMP。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.4
 */
@Getter
@RequiredArgsConstructor
public enum DateRangeType {
    DATE(1),

    LOCAL_DATE_TIME(2),

    LOCAL_DATE(3),

    LOCAL_TIME(4),

    TIMESTAMP(5);

    public final int value;

    /**
     * 与整数值比较是否相等
     *
     * @param value 整数值
     * @return 是否相等
     */
    public boolean equals(int value) {
        return this.value == value;
    }
}
