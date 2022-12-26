package com.ck.reusable.springboot.domain.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "충북대학교 GA팀 - 리유저블 앱 REST API 명세서",
                description = "충북대학교 리유저블 앱 서비스 REST API 명세서 [해당 문서 주소의 api 적힌 부분은 구별을 위한 것이니 사용하실 때, 제외하고 사용 ]",
                version = "v1")
)

@Configuration
public class Swagger {
    @Bean
    public GroupedOpenApi ReusableApi() {
        String[] paths = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("리유저블 앱 서비스 v1")
                .pathsToMatch(paths)
                .packagesToScan("com.ck")
                .displayName("HI")
                .build();
    }
}
