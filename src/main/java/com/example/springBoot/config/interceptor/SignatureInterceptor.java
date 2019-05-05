package com.example.springBoot.config.interceptor;


import com.example.springBoot.config.filter.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

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
        Long startTime = System.currentTimeMillis();
        request.setAttribute("startTime",startTime);
        log.info("[preHandle] executing... request uri is {}", request.getRequestURI());
        if (isJson(request)) {
            // 获取json字符串
            String jsonParam = new RequestWrapper(request).getBodyString();
            log.info("[preHandle] json数据 : {}", jsonParam);
            // 验签逻辑...略...
        }
        try {
            if(handler instanceof HandlerMethod){
                StringBuilder sb = new StringBuilder();
                sb.append("----------------------开始时间：")
                        .append(new SimpleDateFormat("hh:mm:ss.SSS")
                                .format(startTime)+"").append("------------\n");
                sb.append("Controller：").append(((HandlerMethod) handler).getBean().getClass().getName()).append("\n");
                sb.append("Method：").append(((HandlerMethod) handler).getMethod().getName()).append("\n");
                sb.append("RequestMethod：").append(request.getMethod()).append("\n");
                //通过输入流获取POST请求中的参数
                sb.append("Params：").append(new RequestWrapper(request).getBodyString()).append("\n");
                sb.append("Params：").append(getParamString(request.getParameterMap())).append("\n");
                sb.append("URL：").append(request.getRequestURL()).append("\n");
                sb.append("SessionID：").append(request.getSession().getId()).append("\n");
                sb.append("ApplyID：").append(request.getSession().getAttribute("applyId")).append("\n");
                sb.append("OpenID：").append(request.getSession().getAttribute("openid")).append("\n");
                log.info(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long startTime = (Long)request.getAttribute("startTime");
        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;
        if(handler instanceof HandlerMethod){
            StringBuilder sb = new StringBuilder();
            sb.append("CostTime：").append(costTime).append("ms");
            log.info(sb.toString());
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

    private String getParamString(Map<String, String[]> map) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String[]> e : map.entrySet()) {
            sb.append(e.getKey()).append("=");
            String[] value = e.getValue();
            if (value != null && value.length == 1) {
                sb.append(value[0]).append("\t");
            } else {
                sb.append(Arrays.toString(value)).append("\t");
            }
        }
        return sb.toString();
    }
}