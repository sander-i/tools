package org.tools.bedrock.http.req;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * // spring boot 注入拦截器实现requestBody复用
 * @Configuration
 * public class CoreConfig {
 *
 *     @Bean
 *     public FilterRegistrationBean<BodyReaderRequestFilter> coreFilter() {
 *         FilterRegistrationBean<BodyReaderRequestFilter> registrationBean = new FilterRegistrationBean<>();
 *         registrationBean.setFilter(new BodyReaderRequestFilter());
 *         return registrationBean;
 *     }
 * }
 *
 * @author sander
 * @version 1.0
 * @date 2023/10/21 15:45
 * @description 拦截所有请求对request进行body复用封装
 * @scope http
 */
public class BodyReaderRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new BodyReaderRequestWrapper((HttpServletRequest) req), res);
    }

    @Override
    public void destroy() {

    }
}