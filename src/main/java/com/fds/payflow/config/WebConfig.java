package com.fds.payflow.config;

import com.fds.payflow.interceptor.LoginCheckInterceptor;
import com.fds.payflow.utils.SimpleEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/logout",
                        "/login",
                        "/members/add",
                        "/css/**",
                        "/*.ico",
                        "/error"
                );
    }
}
