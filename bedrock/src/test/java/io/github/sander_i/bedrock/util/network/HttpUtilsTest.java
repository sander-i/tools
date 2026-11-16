package io.github.sander_i.bedrock.util.network;

import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * HttpUtils 冒烟测试
 *
 * <p>
 * 使用 JDK 自带的 {@link HttpServer} 启动本地回环服务，不依赖任何外部网站。
 * 早期版本依赖 httpbin.org，在没有外网或该服务不可达时必然失败，
 * 不适合放进默认构建。
 * </p>
 *
 * @author sander
 */
public class HttpUtilsTest {

    private static HttpServer server;
    private static String baseUrl;

    @BeforeClass
    public static void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);

        // 回显请求方法与查询串
        server.createContext("/get", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String body = "{\"url\":\"/get\",\"query\":\"" + (query == null ? "" : query) + "\"}";
            respond(exchange, body);
        });

        // 回显请求体
        server.createContext("/post", exchange -> {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (InputStream in = exchange.getRequestBody()) {
                byte[] chunk = new byte[1024];
                int n;
                while ((n = in.read(chunk)) != -1) {
                    buffer.write(chunk, 0, n);
                }
            }
            String requestBody = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
            String body = "{\"url\":\"/post\",\"body\":\"" + requestBody.replace("\"", "'") + "\"}";
            respond(exchange, body);
        });

        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, String body) throws java.io.IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void testHttpGet() {
        String result = HttpUtils.httpGet(baseUrl + "/get", new HashMap<>());
        assertNotNull("HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("\"url\""));
    }

    @Test
    public void testHttpPost() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");
        requestBody.put("name", "HttpUtilsTest");

        String result = HttpUtils.httpPost(baseUrl + "/post", new HashMap<>(), requestBody);
        assertNotNull("HTTP POST 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("\"url\""));
        assertTrue("响应应包含请求体中的数据", result.contains("test"));
    }

    @Test
    public void testHttpGetWithParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        String result = HttpUtils.httpGet(baseUrl + "/get", params);
        assertNotNull("带参数的 HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含查询参数", result.contains("key1=value1") || result.contains("value1"));
    }

    @Test
    public void testHttpPostWithStringBody() {
        String requestBody = "{\"message\": \"Hello World\", \"test\": true}";

        String result = HttpUtils.httpPost(baseUrl + "/post", new HashMap<>(), requestBody);
        assertNotNull("带字符串请求体的 POST 请求应返回非空结果", result);
        assertTrue("响应应包含请求体内容", result.contains("Hello World"));
    }
}
