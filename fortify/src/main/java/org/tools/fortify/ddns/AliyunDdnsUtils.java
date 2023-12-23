package org.tools.fortify.ddns;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.log4j.Log4j2;
import org.tools.fortify.ddns.aliyun.AliyunDdnsConfig;
import org.tools.fortify.ddns.aliyun.Record;
import org.tools.bedrock.util.BaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xietao
 * @version 1.0
 * @date 2023/10/23 21:04
 * @description 阿里云ddns工具
 * @scope all
 */
@Log4j2
public class AliyunDdnsUtils {

    private AliyunDdnsUtils() {
    }

    private static final String DOMAIN_RECORD_DUPLICATE = "DomainRecordDuplicate";
    private static final String CODE = "Code";

    public static com.aliyun.alidns20150109.Client createClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret).setEndpoint(endpoint);
        return new com.aliyun.alidns20150109.Client(config);
    }

    public static com.aliyun.alidns20150109.Client createClient(AliyunDdnsConfig aliyunDdnsConfig) throws Exception {
        Config config = new Config().setAccessKeyId(aliyunDdnsConfig.getAccessKeyId()).setAccessKeySecret(aliyunDdnsConfig.getAccessKeySecret()).setEndpoint(aliyunDdnsConfig.getEndpoint());
        return new com.aliyun.alidns20150109.Client(config);
    }

    /**
     * 读取域记录
     *
     * @param aliyunDdnsConfig
     * @param domainName       域名
     * @param type             域名前缀
     * @return
     * @throws Exception
     */
    public static List<Record> readDomainRecords(AliyunDdnsConfig aliyunDdnsConfig, String domainName, String type) throws Exception {
        com.aliyun.alidns20150109.Client client = createClient(aliyunDdnsConfig);
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest()
                .setDomainName(domainName);
        if (BaseUtils.isNotEmpty(type)) {
            describeDomainRecordsRequest.setType(type);
        }
        DescribeDomainRecordsResponse describeDomainRecordsResponse = client.describeDomainRecords(describeDomainRecordsRequest);

        List<Record> recordList = new ArrayList<>();
        // 数据量过多会有性能问题
        describeDomainRecordsResponse.getBody().getDomainRecords().getRecord().forEach(r ->
                recordList.add(((JSONObject) JSON.toJSON(r)).toJavaObject(Record.class))
        );
        return recordList;
    }

    /**
     * 更新域记录
     *
     * @param aliyunDdnsConfig
     * @param newRecord        新域记录
     * @return
     * @throws Exception
     */
    public static Record updateDomainRecord(AliyunDdnsConfig aliyunDdnsConfig, Record newRecord) throws Exception {
        com.aliyun.alidns20150109.Client client = createClient(aliyunDdnsConfig);

        UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest()
                //记录RecordId
                .setRecordId(newRecord.getRecordId())
                .setValue(newRecord.getValue())
                .setType(newRecord.getType())
                .setTTL(newRecord.getTTL())
                .setLine(newRecord.getLine())
                .setRR(newRecord.getRR());

        RuntimeOptions runtime = new RuntimeOptions();
        // 1秒超时
        runtime.readTimeout = 1000;
        try {
            client.updateDomainRecordWithOptions(updateDomainRecordRequest, runtime);
        } catch (TeaException e) {
            Map<String, Object> data = e.getData();
            if (DOMAIN_RECORD_DUPLICATE.equals(data.get(CODE))) {
                log.warn("域记录重复");
            }
            log.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return newRecord;
    }

    /**
     * 更新域记录
     *
     * @param aliyunDdnsConfig
     * @param newRecords       新域记录
     * @return
     * @throws Exception
     */
    public static List<Record> updateDomainRecordBatch(AliyunDdnsConfig aliyunDdnsConfig, List<Record> newRecords) throws Exception {
        com.aliyun.alidns20150109.Client client = createClient(aliyunDdnsConfig);
        List<Record> succeedList = new ArrayList<>();
        newRecords.forEach(r -> {
            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest()
                    //记录RecordId
                    .setRecordId(r.getRecordId())
                    .setValue(r.getValue())
                    .setType(r.getType())
                    .setTTL(r.getTTL())
                    .setLine(r.getLine())
                    .setRR(r.getRR());

            RuntimeOptions runtime = new RuntimeOptions();
            // 1秒超时
            runtime.readTimeout = 1000;
            try {
                client.updateDomainRecordWithOptions(updateDomainRecordRequest, runtime);
                succeedList.add(r);
            } catch (TeaException e) {
                Map<String, Object> data = e.getData();
                if (DOMAIN_RECORD_DUPLICATE.equals(data.get(CODE))) {
                    log.warn(DOMAIN_RECORD_DUPLICATE);
                }
                log.error(e.getLocalizedMessage(), e);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        });
        return succeedList;
    }

}
