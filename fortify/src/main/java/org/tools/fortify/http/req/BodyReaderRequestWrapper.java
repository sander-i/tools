package org.tools.fortify.http.req;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * requestBody复用实现
 *
 * @author sander
 * @version 1.0
 * @since 1.0.2
 */
@Slf4j
public class BodyReaderRequestWrapper extends HttpServletRequestWrapper {
    private final String body;

    /**
     * @param request 请求
     */
    public BodyReaderRequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder bodyBuf = new StringBuilder();
        try (InputStream ins = request.getInputStream(); BufferedReader isr = new BufferedReader(new InputStreamReader(
                ins))) {
            char[] charBuffer = new char[128];
            int readCount;
            while ((readCount = isr.read(charBuffer)) != -1) {
                bodyBuf.append(charBuffer, 0, readCount);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        body = bodyBuf.toString();
    }

    /** 返回基于缓存请求体的字符流（支持重复读） */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /** 返回基于缓存请求体的字节流（支持重复读） */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayIns = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            /** 流是否读取完毕 */
            @Override
            public boolean isFinished() {
                return false;
            }

            /** 流是否就绪 */
            @Override
            public boolean isReady() {
                return false;
            }

            /** 注册异步读监听（非异步场景不适用，忽略） */
            @Override
            public void setReadListener(ReadListener readListener) {
            }

            /** 从缓存的请求体中读取一个字节 */
            @Override
            public int read() {
                return byteArrayIns.read();
            }
        };
    }
}
