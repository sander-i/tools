package org.tools.bedrock.http;
/**
 * HTTP 常量：默认连接/读取超时、请求头与字符集等。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
public class HttpConsts {
    private HttpConsts() {
    }

    public static final int DEFAULT_CONNECT_TIMEOUT = 6000;

    public static final int DEFAULT_READ_TIMEOUT = 3000;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DEFAULT_REQUEST_METHOD = "GET";

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String CODE = "code";

    public static final String MESSAGE = "message";

    public static final String STATUS = "status";

    public static final String MSG = "msg";

    public static final String DATA = "data";
}
