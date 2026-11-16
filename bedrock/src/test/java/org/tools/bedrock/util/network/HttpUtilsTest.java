package org.tools.bedrock.util.network;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * HttpUtils 冒烟测试
 * 测试基本的 HTTP GET 和 POST 请求功能
 */
public class HttpUtilsTest {

    @Test
    public void testHttpGet() {
        // 测试基本的 HTTP GET 请求
        String result = HttpUtils.httpGet("https://httpbin.org/get", new HashMap<>());
        assertNotNull("HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("\"url\""));
    }

    @Test
    public void testHttpPost() {
        // 测试基本的 HTTP POST 请求
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");
        requestBody.put("name", "HttpUtilsTest");

        String result = HttpUtils.httpPost("https://httpbin.org/post", new HashMap<>(), requestBody);
        assertNotNull("HTTP POST 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("\"url\""));
        assertTrue("响应应包含请求体中的数据", result.contains("\"test\""));
    }

    @Test
    public void testHttpGetWithParams() {
        // 测试带参数的 HTTP GET 请求
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        String result = HttpUtils.httpGet("https://httpbin.org/get", params);
        assertNotNull("带参数的 HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含查询参数", result.contains("key1=value1") || result.contains("value1"));
    }

    @Test
    public void testHttpPostWithStringBody() {
        // 测试带字符串请求体的 POST 请求
        String requestBody = "{\"message\": \"Hello World\", \"test\": true}";

        String result = HttpUtils.httpPost("https://httpbin.org/post", new HashMap<>(), requestBody);
        assertNotNull("带字符串请求体的 POST 请求应返回非空结果", result);
        assertTrue("响应应包含请求体内容", result.contains("Hello World"));
    }
}
