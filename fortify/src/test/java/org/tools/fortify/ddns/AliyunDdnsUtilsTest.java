package org.tools.fortify.ddns;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.alidns20150109.models.UpdateDomainRecordResponse;
import com.aliyun.tea.TeaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.tools.fortify.ddns.aliyun.AliyunDdnsConfig;
import org.tools.fortify.ddns.aliyun.Record;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AliyunDdnsUtils 冒烟测试
 * 测试阿里云DDNS工具类的基本功能
 */
@RunWith(MockitoJUnitRunner.class)
public class AliyunDdnsUtilsTest {

    @Mock
    private Client mockClient;

    private AliyunDdnsConfig config;
    private static final String TEST_ACCESS_KEY_ID = "testAccessKeyId";
    private static final String TEST_ACCESS_KEY_SECRET = "testAccessKeySecret";
    private static final String TEST_ENDPOINT = "testEndpoint";
    private static final String TEST_DOMAIN_NAME = "example.com";
    private static final String TEST_RECORD_TYPE = "A";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        config = new AliyunDdnsConfig();
        config.setAccessKeyId(TEST_ACCESS_KEY_ID);
        config.setAccessKeySecret(TEST_ACCESS_KEY_SECRET);
        config.setEndpoint(TEST_ENDPOINT);
    }

    @Test
    public void testCreateClientWithParameters() throws Exception {
        // 测试使用参数创建客户端
        Client client = AliyunDdnsUtils.createClient(TEST_ACCESS_KEY_ID, TEST_ACCESS_KEY_SECRET, TEST_ENDPOINT);
        assertNotNull("创建的客户端不应为null", client);
    }

    @Test
    public void testCreateClientWithConfig() throws Exception {
        // 测试使用配置对象创建客户端
        Client client = AliyunDdnsUtils.createClient(config);
        assertNotNull("创建的客户端不应为null", client);
    }

    @Test
    public void testReadDomainRecords() throws Exception {
        // 测试读取域记录功能
        // 模拟API响应
        DescribeDomainRecordsResponse mockResponse = mock(DescribeDomainRecordsResponse.class);
        DescribeDomainRecordsResponseBody mockBody = mock(DescribeDomainRecordsResponseBody.class);
        DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords mockDomainRecords =
            mock(DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords.class);

        when(mockClient.describeDomainRecords(any(DescribeDomainRecordsRequest.class)))
            .thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockBody.getDomainRecords()).thenReturn(mockDomainRecords);
        when(mockDomainRecords.getRecord()).thenReturn(java.util.Collections.emptyList());

        List<Record> records = AliyunDdnsUtils.readDomainRecords(config, TEST_DOMAIN_NAME, TEST_RECORD_TYPE);

        assertNotNull("返回的记录列表不应为null", records);
        assertTrue("记录列表应为空", records.isEmpty());

        // 验证方法被调用
        verify(mockClient).describeDomainRecords(any(DescribeDomainRecordsRequest.class));
    }

    @Test
    public void testReadDomainRecordsWithNullType() throws Exception {
        // 测试读取域记录功能，类型为null
        DescribeDomainRecordsResponse mockResponse = mock(DescribeDomainRecordsResponse.class);
        DescribeDomainRecordsResponseBody mockBody = mock(DescribeDomainRecordsResponseBody.class);
        DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords mockDomainRecords =
            mock(DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords.class);

        when(mockClient.describeDomainRecords(any(DescribeDomainRecordsRequest.class)))
            .thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockBody.getDomainRecords()).thenReturn(mockDomainRecords);
        when(mockDomainRecords.getRecord()).thenReturn(java.util.Collections.emptyList());

        List<Record> records = AliyunDdnsUtils.readDomainRecords(config, TEST_DOMAIN_NAME, null);

        assertNotNull("返回的记录列表不应为null", records);
        assertTrue("记录列表应为空", records.isEmpty());

        // 验证方法被调用
        verify(mockClient).describeDomainRecords(any(DescribeDomainRecordsRequest.class));
    }

    @Test
    public void testUpdateDomainRecord() throws Exception {
        // 测试更新域记录功能
        Record testRecord = new Record();
        testRecord.setRecordId("testRecordId");
        testRecord.setValue("192.168.1.1");
        testRecord.setType("A");
        testRecord.setTTL(600L);
        testRecord.setLine("default");
        testRecord.setRR("www");

        // 模拟API响应
        UpdateDomainRecordResponse mockResponse = mock(UpdateDomainRecordResponse.class);

        when(mockClient.updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any()))
            .thenReturn(mockResponse);

        Record updatedRecord = AliyunDdnsUtils.updateDomainRecord(config, testRecord);

        assertNotNull("返回的记录不应为null", updatedRecord);
        assertSame("应返回相同的记录对象", testRecord, updatedRecord);

        // 验证方法被调用
        verify(mockClient).updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any());
    }

    @Test
    public void testUpdateDomainRecordBatch() throws Exception {
        // 测试批量更新域记录功能
        Record testRecord1 = new Record();
        testRecord1.setRecordId("testRecordId1");
        testRecord1.setValue("192.168.1.1");
        testRecord1.setType("A");
        testRecord1.setTTL(600L);
        testRecord1.setLine("default");
        testRecord1.setRR("www");

        Record testRecord2 = new Record();
        testRecord2.setRecordId("testRecordId2");
        testRecord2.setValue("192.168.1.2");
        testRecord2.setType("A");
        testRecord2.setTTL(600L);
        testRecord2.setLine("default");
        testRecord2.setRR("mail");

        java.util.List<Record> records = java.util.Arrays.asList(testRecord1, testRecord2);

        // 模拟API响应
        UpdateDomainRecordResponse mockResponse = mock(UpdateDomainRecordResponse.class);

        when(mockClient.updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any()))
            .thenReturn(mockResponse);

        List<Record> updatedRecords = AliyunDdnsUtils.updateDomainRecordBatch(config, records);

        assertNotNull("返回的记录列表不应为null", updatedRecords);
        assertEquals("应返回两个记录", 2, updatedRecords.size());

        // 验证方法被调用两次
        verify(mockClient, times(2)).updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any());
    }

    @Test
    public void testUpdateDomainRecordBatchWithException() throws Exception {
        // 测试批量更新域记录功能，其中一个记录更新失败
        Record testRecord1 = new Record();
        testRecord1.setRecordId("testRecordId1");
        testRecord1.setValue("192.168.1.1");
        testRecord1.setType("A");
        testRecord1.setTTL(600L);
        testRecord1.setLine("default");
        testRecord1.setRR("www");

        Record testRecord2 = new Record();
        testRecord2.setRecordId("testRecordId2");
        testRecord2.setValue("192.168.1.2");
        testRecord2.setType("A");
        testRecord2.setTTL(600L);
        testRecord2.setLine("default");
        testRecord2.setRR("mail");

        java.util.List<Record> records = java.util.Arrays.asList(testRecord1, testRecord2);

        // 模拟第一次调用成功，第二次调用失败
        UpdateDomainRecordResponse mockResponse = mock(UpdateDomainRecordResponse.class);

        when(mockClient.updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any()))
            .thenReturn(mockResponse) // 第一次调用返回成功
            .thenThrow(new TeaException(java.util.Collections.singletonMap("message", "模拟API错误"))); // 第二次调用抛出异常

        try {
            AliyunDdnsUtils.updateDomainRecordBatch(config, records);
            fail("应抛出异常");
        } catch (Exception e) {
            // 陴期会捕获到异常
            assertTrue("应抛出Exception或其子类", e instanceof Exception);
        }

        // 验证方法被调用两次
        verify(mockClient, times(2)).updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any());
    }

    @Test
    public void testReadDomainRecordsWithException() throws Exception {
        // 测试读取域记录功能，API调用抛出异常
        when(mockClient.describeDomainRecords(any(DescribeDomainRecordsRequest.class)))
            .thenThrow(new TeaException(java.util.Collections.singletonMap("message", "模拟API错误")));

        try {
            AliyunDdnsUtils.readDomainRecords(config, TEST_DOMAIN_NAME, TEST_RECORD_TYPE);
            fail("应抛出异常");
        } catch (Exception e) {
            // 陴期会捕获到异常
            assertTrue("应抛出Exception或其子类", e instanceof Exception);
        }
    }

    @Test
    public void testUpdateDomainRecordWithException() throws Exception {
        // 测试更新域记录功能，API调用抛出异常
        Record testRecord = new Record();
        testRecord.setRecordId("testRecordId");
        testRecord.setValue("192.168.1.1");
        testRecord.setType("A");
        testRecord.setTTL(600L);
        testRecord.setLine("default");
        testRecord.setRR("www");

        when(mockClient.updateDomainRecordWithOptions(any(UpdateDomainRecordRequest.class), any()))
            .thenThrow(new TeaException(java.util.Collections.singletonMap("message", "模拟API错误")));

        try {
            AliyunDdnsUtils.updateDomainRecord(config, testRecord);
            fail("应抛出异常");
        } catch (Exception e) {
            // 陴期会捕获到异常
            assertTrue("应抛出Exception或其子类", e instanceof Exception);
        }
    }
}
