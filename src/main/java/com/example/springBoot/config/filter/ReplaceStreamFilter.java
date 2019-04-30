package com.example.springBoot.config.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 01
 * @program wrapper-demo
 * @description 替换HttpServletRequest
 * @create 2018-12-24 21:04
 * @since 1.0
 **/

public class ReplaceStreamFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(ReplaceStreamFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("StreamFilter初始化...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        log.info("StreamFilter销毁...");
    }
}