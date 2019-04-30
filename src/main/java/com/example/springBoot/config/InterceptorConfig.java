package com.example.springBoot.config;

import com.example.springBoot.config.interceptor.SignatureInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 01
 * @program wrapper-demo
 * @description
 * @create 2018-12-24 21:16
 * @since 1.0
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public SignatureInterceptor getSignatureInterceptor(){
        return new SignatureInterceptor();
    }

    /**
     * 注册拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSignatureInterceptor())
                .addPathPatterns("/**");
    }
}