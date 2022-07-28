package com.ck.reusable.springboot.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter1> filter1(){
        FilterRegistrationBean<Filter1> bean = new FilterRegistrationBean<>(new Filter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번호가 다수의 필터 중 가장 먼저 실행
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter2> filter2(){
        FilterRegistrationBean<Filter2> bean = new FilterRegistrationBean<>(new Filter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // 낮은 번호가 다수의 필터 중 가장 먼저 실행
        return bean;
    }
}
