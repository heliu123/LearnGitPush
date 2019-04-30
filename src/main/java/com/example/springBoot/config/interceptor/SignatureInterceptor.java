package com.example.springBoot.config.interceptor;


import com.example.springBoot.config.filter.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 01
 * @program wrapper-demo
 * @description 签名拦截器
 * @create 2018-12-24 21:08
 * @since 1.0
 **/

public class SignatureInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(SignatureInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle] executing... request uri is {}", request.getRequestURI());
        if (isJson(request)) {
            // 获取json字符串
            String jsonParam = new RequestWrapper(request).getBodyString();
            log.info("[preHandle] json数据 : {}", jsonParam);

            // 验签逻辑...略...
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (isJson(request)) {

            // 获取json字符串

            log.info("[postHandle] json数据 : {}", handler);

            // 验签逻辑...略...
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJson(HttpServletRequest request) {
        if (request.getContentType() != null) {
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        return false;
    }
}