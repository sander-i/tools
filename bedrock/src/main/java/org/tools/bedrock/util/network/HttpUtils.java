package org.tools.bedrock.util.network;

import lombok.extern.slf4j.Slf4j;
import org.tools.bedrock.constant.SpecialSymbolConsts;
import org.tools.bedrock.http.HttpConsts;
import org.tools.bedrock.util.BaseUtils;
import org.tools.bedrock.util.JsonUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/28 20:28
 * @description 原生java http工具
 * @scope all
 */
@Slf4j
public class HttpUtils {

    private HttpUtils() {
    }

    public static String httpPost(String url, Map<String, String> headers, Map<String, ?> requestBody) {
        return httpPost(url, headers, null, requestBody);
    }

    public static String httpPost(String url, Map<String, String> headers, String requestBody) {
        return httpPost(url, headers, null, requestBody);
    }

    public static String httpPost(String url, Map<String, String> headers, Map<String, ?> requestParam, Map<String, ?> requestBody) {
        return httpPost(url, headers, requestParam, requestBody, HttpConsts.DEFAULT_CHARSET);
    }

    public static String httpPost(String url, Map<String, String> headers, Map<String, ?> requestParam, String requestBody) {
        return httpPost(url, headers, requestParam, requestBody, HttpConsts.DEFAULT_CHARSET);
    }

    public static String httpPost(String url, Map<String, String> headers, Map<String, ?> requestParam, Map<String, ?> requestBody, String charset) {
        return httpPost(url, headers, requestParam, JsonUtils.mapToJSON(requestBody), charset);
    }

    public static String httpPost(String url, Map<String, String> headers, Map<String, ?> requestParam, String requestBody, String charset) {
        return httpPost(url, HttpConsts.DEFAULT_CONNECT_TIMEOUT, HttpConsts.DEFAULT_READ_TIMEOUT, headers, requestParam, requestBody, charset);
    }

    /**
     * http 请求(post 方式)
     *
     * @param url            请求地址
     * @param connectTimeout 连接超时时长
     * @param readTimeout    响应超时时长
     * @param headers        请求标头
     * @param requestParam   请求参数
     * @param requestBody    请求正文
     * @param charset        响应编码格式
     * @return 响应报文
     */
    public static String httpPost(String url, int connectTimeout, int readTimeout, Map<String, String> headers, Map<String, ?> requestParam, String requestBody, String charset) {
        String result = null;
        url = setRequestParam(url, requestParam);
        try {
            result = httpRequest(url, connectTimeout, readTimeout, HttpConsts.POST, headers, requestBody, charset);
        } catch (IOException e) {
            log.error("httpPOST", e);
        }
        return result;
    }

    public static String httpGet(String url, Map<String, ?> requestParam) {
        return httpGet(url, requestParam, HttpConsts.DEFAULT_CHARSET);
    }

    public static String httpGet(String url, String requestBody) {
        return httpGet(url, requestBody, HttpConsts.DEFAULT_CHARSET);
    }

    public static String httpGet(String url, String requestBody, String charset) {
        return httpGet(url, null, requestBody, charset);
    }

    public static String httpGet(String url, Map<String, ?> requestParam, String requestBody) {
        return httpGet(url, null, requestParam, requestBody);
    }

    public static String httpGet(String url, Map<String, String> headers, Map<String, ?> requestParam) {
        return httpGet(url, headers, requestParam, null);
    }

    public static String httpGet(String url, Map<String, ?> requestParam, String requestBody, String charset) {
        return httpGet(url, null, requestParam, requestBody, charset);
    }

    public static String httpGet(String url, Map<String, String> headers, Map<String, ?> requestParam, String requestBody) {
        return httpGet(url, headers, requestParam, requestBody, HttpConsts.DEFAULT_CHARSET);
    }

    public static String httpGet(String url, Map<String, String> headers, Map<String, ?> requestParam, String requestBody, String charset) {
        return httpGet(url, HttpConsts.DEFAULT_CONNECT_TIMEOUT, HttpConsts.DEFAULT_READ_TIMEOUT, headers, requestParam, requestBody, charset);
    }

    /**
     * http 请求(get 方式)
     *
     * @param url            请求地址
     * @param connectTimeout 连接超时时长
     * @param readTimeout    响应超时时长
     * @param headers        请求标头
     * @param requestParam   请求参数
     * @param requestBody    请求正文
     * @param charset        响应编码格式
     * @return 响应报文
     */
    public static String httpGet(String url, int connectTimeout, int readTimeout, Map<String, String> headers, Map<String, ?> requestParam, String requestBody, String charset) {
        String result = null;
        url = setRequestParam(url, requestParam);
        try {
            result = httpRequest(url, connectTimeout, readTimeout, HttpConsts.GET, headers, requestBody, charset);
        } catch (IOException e) {
            log.error("httpGet", e);
        }
        return result;
    }

    /**
     * http 请求
     *
     * @param url            请求地址
     * @param connectTimeout 连接超时时长
     * @param readTimeout    响应超时时长
     * @param requestMethod  请求方式
     * @param headers        请求标头
     * @param requestBody    请求正文
     * @param charset        响应编码格式
     * @return 响应报文
     * @throws IOException http 请求异常
     */
    public static String httpRequest(String url, int connectTimeout, int readTimeout, String requestMethod, Map<String, String> headers, String requestBody, String charset) throws IOException {
        HttpURLConnection httpConn = getHttpURLConnection(url, connectTimeout, readTimeout, requestMethod, headers);

        httpConn.connect();

        if (!HttpConsts.GET.equals(requestMethod)) {
            setRequestBody(httpConn, requestBody);
        }
        return responseResult(httpConn, charset);
    }

    /**
     * 获取 url 连接
     *
     * @param uri            请求地址
     * @param connectTimeout 连接超时时长
     * @param readTimeout    响应超时时长
     * @param headers        请求标头
     * @return url 连接
     * @throws IOException url 连接异常
     */
    public static URLConnection getURLConnection(String uri, int connectTimeout, int readTimeout, Map<String, String> headers) throws IOException {
        URL url = new URL(uri);
        URLConnection urlConn = url.openConnection();
        urlConn.setConnectTimeout(connectTimeout);
        urlConn.setReadTimeout(readTimeout);
        headers = defaultRequestHeaders(headers);
        headers.forEach(urlConn::setRequestProperty);
        return urlConn;
    }

    /**
     * 获取 http 连接
     *
     * @param url            请求地址
     * @param connectTimeout 连接超时时长
     * @param readTimeout    响应超时时长
     * @param requestMethod  请求方式
     * @param headers        请求标头
     * @return http 连接
     * @throws IOException http 连接异常
     */
    public static HttpURLConnection getHttpURLConnection(String url, int connectTimeout, int readTimeout, String requestMethod, Map<String, String> headers) throws IOException {
        HttpURLConnection httpUrlConn = (HttpURLConnection) getURLConnection(url, connectTimeout, readTimeout, headers);

        httpUrlConn.setRequestMethod(requestMethod);
        if (HttpConsts.POST.equals(requestMethod)) {
            // 打开输出流
            httpUrlConn.setDoOutput(true);
            // 关闭缓存
            httpUrlConn.setUseCaches(false);
        }
        return httpUrlConn;
    }

    /**
     * 默认请求标头
     *
     * @param headers 请求标头
     * @return 请求标头
     */
    public static Map<String, String> defaultRequestHeaders(Map<String, String> headers) {
        if (BaseUtils.isEmpty(headers)) {
            // 通用的请求属性
            headers = new HashMap<>();
            headers.put("accept", "*/*");
            headers.put("connection", "Keep-Alive");
            headers.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            headers.put("Content-Type", "application/json;charset=utf-8");
        }
        return headers;
    }

    /**
     * 设置请求参数
     *
     * @param url          请求地址
     * @param requestParam 请求参数
     * @return 请求地址
     */
    public static String setRequestParam(String url, Map<String, ?> requestParam) {
        if (BaseUtils.isNotEmpty(requestParam)) {
            StringBuilder urlParam = new StringBuilder(url).append(SpecialSymbolConsts.QUESTION_MARK);
            requestParam.forEach((k, v) -> urlParam.append(k).append(SpecialSymbolConsts.BE_EQUAL_TO).append(v));
            return urlParam.toString();
        }
        return url;
    }

    /**
     * 请求响应结果
     *
     * @param httpConn http连接
     * @param charset  响应编码格式
     * @return 响应结果
     * @throws IOException 开启输入流异常
     */
    public static String responseResult(HttpURLConnection httpConn, String charset) throws IOException {
        InputStream input;
        int responseCode = httpConn.getResponseCode();
        if (200 == responseCode) {
            input = httpConn.getInputStream();
        } else {
            input = httpConn.getErrorStream();
        }

        if (charset == null) {
            charset = String.valueOf(StandardCharsets.UTF_8);
        }

        StringBuilder result = new StringBuilder();
        if (input != null) {
            InputStreamReader inputReader = new InputStreamReader(input, charset);
            BufferedReader br = new BufferedReader(inputReader);
            String temp;
            while (null != (temp = br.readLine())) {
                result.append(temp);
            }
            inputRelease(input, br, inputReader);
        } else {
            httpResponse(httpConn, result);
        }
        httpConn.disconnect();
        return result.toString();
    }

    /**
     * 设置请求正文
     *
     * @param httpConn    http连接
     * @param requestBody 请求正文
     * @throws IOException 开启输出流异常
     */
    public static void setRequestBody(HttpURLConnection httpConn, String requestBody) throws IOException {
        OutputStream output = httpConn.getOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(output);
        outputWriter.write(requestBody);
        outputWriter.flush();

        outputRelease(output, outputWriter);
    }

    /**
     * 结果错误
     *
     * @param httpConn http连接
     * @param result   结果
     * @throws IOException 响应异常
     */
    public static void httpResponse(HttpURLConnection httpConn, StringBuilder result) throws IOException {
        result.append(SpecialSymbolConsts.LEFT_BRACE);
        // "code"
        result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append("code").append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
        // :
        result.append(SpecialSymbolConsts.COLON);
        // "httpConn.getResponseCode()"
        result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(httpConn.getResponseCode()).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
        // ,
        result.append(SpecialSymbolConsts.SEMICOLON);
        // "message"
        result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append("message").append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
        // :
        result.append(SpecialSymbolConsts.COLON);
        // "httpConn.getResponseMessage()"
        result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(httpConn.getResponseMessage()).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
        result.append(SpecialSymbolConsts.RIGHT_BRACE);
    }

    /**
     * 输出流释放
     *
     * @param output 输出流
     * @param writer 写流
     * @throws IOException 释放流异常
     */
    public static void outputRelease(OutputStream output, Writer... writer) throws IOException {
        for (Writer value : writer) {
            value.close();
        }
        output.close();
    }

    /**
     * 输入流释放
     *
     * @param input  输入流
     * @param reader 读流
     * @throws IOException 释放流异常
     */
    public static void inputRelease(InputStream input, Reader... reader) throws IOException {
        for (Reader value : reader) {
            value.close();
        }
        input.close();
    }

}
