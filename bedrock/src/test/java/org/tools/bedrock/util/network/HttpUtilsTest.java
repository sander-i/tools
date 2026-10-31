package org.tools.bedrock.util.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * HttpUtils 冒烟测试：使用本地 HttpServer 验证 GET / POST 请求与参数拼接。
 *
 * <p>不依赖外部网络端点，保证在离线环境与 CI 中稳定运行。</p>
 *
 * @author sander
 * @since 1.0.6.6
 */
public class HttpUtilsTest {
    /**
     * 本地测试服务
     */
    private static HttpServer server;

    /**
     * 本地测试服务端口
     */
    private static int port;

    /**
     * 启动本地 HttpServer（随机端口）
     *
     * @throws IOException 端口绑定失败时抛出
     */
    @BeforeClass
    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/get", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getRawQuery();
                respond(exchange, "url=/get&query=" + (query == null ? "" : query));
            }
        });
        server.createContext("/post", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                respond(exchange, "url=/post&body=" + readAll(exchange.getRequestBody()));
            }
        });
        server.setExecutor(null);
        server.start();
        port = server.getAddress().getPort();
    }

    /**
     * 停止本地 HttpServer
     */
    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    public void testHttpGet() {
        String result = HttpUtils.httpGet(url("/get"), new HashMap<String, Object>());
        assertNotNull("HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("url=/get"));
    }

    @Test
    public void testHttpPost() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");
        requestBody.put("name", "HttpUtilsTest");

        String result = HttpUtils.httpPost(url("/post"), new HashMap<String, String>(), requestBody);
        assertNotNull("HTTP POST 请求应返回非空结果", result);
        assertTrue("响应应包含 URL 信息", result.contains("url=/post"));
        assertTrue("响应应包含请求体中的数据", result.contains("test") && result.contains("value"));
    }

    @Test
    public void testHttpGetWithParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        String result = HttpUtils.httpGet(url("/get"), params);
        assertNotNull("带参数的 HTTP GET 请求应返回非空结果", result);
        assertTrue("响应应包含查询参数", result.contains("key1=value1"));
    }

    @Test
    public void testHttpPostWithStringBody() {
        String requestBody = "{\"message\": \"Hello World\", \"test\": true}";

        String result = HttpUtils.httpPost(url("/post"), new HashMap<String, String>(), requestBody);
        assertNotNull("带字符串请求体的 POST 请求应返回非空结果", result);
        assertTrue("响应应包含请求体内容", result.contains("Hello World"));
    }

    /**
     * 拼接本地测试服务地址
     *
     * @param path 请求路径
     * @return 完整 URL
     */
    private static String url(String path) {
        return "http://127.0.0.1:" + port + path;
    }

    /**
     * 写出响应内容
     *
     * @param exchange HTTP 交换对象
     * @param body     响应正文
     * @throws IOException 写出失败时抛出
     */
    private static void respond(HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream output = exchange.getResponseBody();
        try {
            output.write(bytes);
        } finally {
            output.close();
        }
    }

    /**
     * 读取请求体
     *
     * @param input 输入流
     * @return UTF-8 文本
     * @throws IOException 读取失败时抛出
     */
    private static String readAll(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        int read;
        while ((read = input.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        input.close();
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }
}
