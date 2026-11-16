package io.github.sander_i.fortify.ddns;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.fortify.ddns.aliyun.AliyunDdnsConfig;
import io.github.sander_i.fortify.ddns.aliyun.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云ddns工具
 *
 * @author sander
 * @version 1.0
 */
@Slf4j
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
     * @param aliyunDdnsConfig 阿里云ddns配置
     * @param domainName       域名
     * @param type             域名前缀
     * @return 域记录
     * @throws Exception 异常
     */
    public static List<Record> readDomainRecords(AliyunDdnsConfig aliyunDdnsConfig, String domainName, String type) throws Exception {
        return readDomainRecords(createClient(aliyunDdnsConfig), domainName, type);
    }

    /**
     * 读取域记录（使用外部提供的客户端，便于测试注入）
     *
     * @param client     阿里云 DNS 客户端
     * @param domainName 域名
     * @param type       域名前缀
     * @return 域记录
     * @throws Exception 异常
     */
    public static List<Record> readDomainRecords(com.aliyun.alidns20150109.Client client, String domainName, String type) throws Exception {
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
     * @param aliyunDdnsConfig 阿里云ddns配置
     * @param newRecord        新域记录
     * @return 域记录
     * @throws Exception 异常
     */
    public static Record updateDomainRecord(AliyunDdnsConfig aliyunDdnsConfig, Record newRecord) throws Exception {
        return updateDomainRecord(createClient(aliyunDdnsConfig), newRecord);
    }

    /**
     * 更新域记录（使用外部提供的客户端，便于测试注入）
     *
     * @param client    阿里云 DNS 客户端
     * @param newRecord 新域记录
     * @return 域记录
     * @throws Exception 异常
     */
    public static Record updateDomainRecord(com.aliyun.alidns20150109.Client client, Record newRecord) throws Exception {
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
        client.updateDomainRecordWithOptions(updateDomainRecordRequest, runtime);
        return newRecord;
    }

    /**
     * 更新域记录
     *
     * @param aliyunDdnsConfig 阿里云ddns配置
     * @param newRecords       新域记录
     * @return 域记录
     * @throws Exception 异常
     */
    public static List<Record> updateDomainRecordBatch(AliyunDdnsConfig aliyunDdnsConfig, List<Record> newRecords) throws Exception {
        return updateDomainRecordBatch(createClient(aliyunDdnsConfig), newRecords);
    }

    /**
     * 批量更新域记录（使用外部提供的客户端，便于测试注入）
     *
     * @param client     阿里云 DNS 客户端
     * @param newRecords 新域记录
     * @return 成功的域记录
     * @throws Exception 异常
     */
    public static List<Record> updateDomainRecordBatch(com.aliyun.alidns20150109.Client client, List<Record> newRecords) throws Exception {
        List<Record> succeedList = new ArrayList<>();
        StringBuilder error = new StringBuilder();

        newRecords.forEach(r -> {
            try {
                succeedList.add(updateDomainRecord(client, r));
            } catch (Exception e) {
                error.append(e.getMessage()).append("\n");
            }
        });

        if (BaseUtils.isNotEmpty(error.toString())) {
            log.error("updateDomainRecordBatch error: {}", error);
            throw new Exception(error.substring(0, error.length() - 1));
        }
        return succeedList;
    }

}
