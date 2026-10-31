package org.tools.fortify.http.req;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 拦截所有请求对request进行body复用封装
 *
 * @author sander
 * @version 1.0
 * @since 1.0.2
 */
public class BodyReaderRequestFilter implements Filter {
    /*
        // spring boot 注入拦截器实现requestBody复用
        @Configuration
        public class CoreConfig {
            @Bean
            public FilterRegistrationBean<BodyReaderRequestFilter> coreFilter() {
                FilterRegistrationBean<BodyReaderRequestFilter> registrationBean = new FilterRegistrationBean<>();
                registrationBean.setFilter(new BodyReaderRequestFilter());
                return registrationBean;
            }
        }
     */

    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 将请求包装为可重复读后放行过滤链
     *
     * @throws ServletException Servlet 处理失败时抛出
     * @throws IOException IO 读写失败时抛出
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException,
            IOException {
        filterChain.doFilter(new BodyReaderRequestWrapper((HttpServletRequest) req), res);
    }

    /** 过滤器销毁（无资源需要释放） */
    @Override
    public void destroy() {
    }
}
