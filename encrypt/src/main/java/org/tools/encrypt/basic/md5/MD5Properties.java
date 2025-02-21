package org.tools.encrypt.basic.md5;

import lombok.Data;
import org.tools.encrypt.enums.AlgorithmEnum;

/**
 * @author sander
 * @version 1.0
 */
@Data
public class MD5Properties {

    /**
     * 算法
     */
    private String algorithm = AlgorithmEnum.MD5.getAlgorithm();
    private int signum = 1;
    private int radix = 16;
    private char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String charsetName = "utf-8";

}
