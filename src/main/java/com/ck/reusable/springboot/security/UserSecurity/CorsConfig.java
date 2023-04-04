package com.ck.reusable.springboot.security.UserSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // crossOrigin 으로 해결하지 못하는 문제, 인증이 필요한 api에서 다음과 같은 filte가 필요하다.

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정 ( front와 연동 시 필요 )

//        config.addAllowedOrigin("*"); // 모든 ip에 대한 응답을 허용

        config.addAllowedOriginPattern("*"); // addAllowedOriginPattern("*") 대신 사용

        config.addAllowedHeader("*"); // 모든 header에 대한 응답을 허용

        config.addAllowedMethod("*"); // 모든 전송 방식에 대해서 허용

        // 해당 주소는 모두 config를 따라야한다
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
