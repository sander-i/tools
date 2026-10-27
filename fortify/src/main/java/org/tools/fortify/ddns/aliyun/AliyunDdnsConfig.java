package org.tools.fortify.ddns.aliyun;

import lombok.Data;

/**
 * 阿里云 DNS 配置实体：AccessKey、域名与记录参数。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.2
 */
@Data
public class AliyunDdnsConfig {
    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint = "alidns.cn-shanghai.aliyuncs.com";
}
