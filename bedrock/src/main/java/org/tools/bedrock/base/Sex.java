package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sander
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum Sex {

    /**
     * 男
     */
    M('1'),
    /**
     * 女
     */
    F('0'),
    /**
     * 男
     */
    MAN('1'),
    /**
     * 女
     */
    GIRL('0');

    private final char sex;

}
