package org.tools.bedrock.util.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author sander
 * @version 1.0
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

    /** 与整数值比较是否相等 */
    public boolean equals(int value) {
        return this.value == value;
    }

}
