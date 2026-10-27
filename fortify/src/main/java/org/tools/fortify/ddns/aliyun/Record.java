package org.tools.fortify.ddns.aliyun;

import lombok.Data;

/**
 * 阿里云 DNS 解析记录实体：记录值、类型、TTL 等字段。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.2
 */
@Data
public class Record {
    public String status;

    public String type;

    public String remark;

    public Long TTL;

    public String recordId;

    public Long priority;

    public String RR;

    public String domainName;

    public Integer weight;

    public String value;

    public String line;

    public Boolean locked;
}
