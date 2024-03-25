package org.tools.fortify.ddns.aliyun;

import lombok.Data;

/**
 * @author sander
 * @version 1.0
 * @date 2023/10/23 21:01
 * @description 描述
 * @scope 作用域
 */
@Data
public class AliyunDdnsConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint = "alidns.cn-shanghai.aliyuncs.com";

}
