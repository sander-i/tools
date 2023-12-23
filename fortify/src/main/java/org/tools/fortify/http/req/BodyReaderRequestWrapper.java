package org.tools.fortify.http.req;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author sander
 * @version 1.0
 * @date 2023/10/21 15:39
 * @description requestBody复用实现
 * @scope http
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
        try (InputStream ins = request.getInputStream(); BufferedReader isr = new BufferedReader(new InputStreamReader(ins))) {
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

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayIns = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return byteArrayIns.read();
            }
        };
    }
}
