package org.tools.bedrock.http;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 11:58
 * @description http请求常量
 * @scope http
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
