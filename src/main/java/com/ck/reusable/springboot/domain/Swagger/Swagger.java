package com.ck.reusable.springboot.domain.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@OpenAPIDefinition(
        info = @Info(title = "충북대학교 GA팀 - 리유저블 앱 REST API 명세서",
                description = "충북대학교 리유저블 앱 서비스 REST API 명세서 [해당 문서 주소의 api 적힌 부분은 구별을 위한 것이니 사용하실 때, 제외하고 사용 ] <br><br>"
                + "<참고사항> <br>" + "1. 400 에러 발생시 값들이 제대로 들어갔는지 확인해주세요 <br>" + "2. /api 적힌 부분은 제외하시고 사용하세요 ex) /api/save -> host url/save <br>" +
                "3. 현재 문서에서 권한이 필요한 주소에 따로 Token값이 필요하지 않습니다.",
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
                .displayName("전체")
                .build();
    }

}
