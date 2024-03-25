package org.tools.fortify.ddns.aliyun;

import lombok.Data;

/**
 * @author sander
 * @version 1.0
 * @date 2023/10/27 20:54
 * @description 描述
 * @scope 作用域
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
