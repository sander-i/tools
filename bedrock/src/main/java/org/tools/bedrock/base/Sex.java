package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举。
 *
 * <p>取值沿用既有字符编码约定：男 '1'、女 '0'。注意与 GB/T 2261.1
 * 的数字码（0 未知、1 男、2 女、9 未说明）不同，对接国标场景时调用方需自行转换。
 * 历史版本的同值重复条目 MAN/GIRL 已删除。</p>
 *
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
    F('0');

    private final char sex;

}
