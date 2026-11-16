package io.github.sander_i.fortify.ddns.aliyun;

import lombok.Data;

/**
 * @author sander
 * @version 1.0
 */
@Data
public class AliyunDdnsConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint = "alidns.cn-shanghai.aliyuncs.com";

}
