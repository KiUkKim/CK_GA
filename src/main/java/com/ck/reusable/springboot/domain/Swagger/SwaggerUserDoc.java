package com.ck.reusable.springboot.domain.Swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class SwaggerUserDoc {

    @Schema(description = "회원가입")
    @Getter
    public static class SwaggerUserSignDto {

        @Schema(description = "이름")
        private String name;

        @Schema(description = "이메일", allowableValues = {"xxxx@xxxx.com"})
        private String email;

        @Schema(description = "전화번호", allowableValues = {"010-0000-0000"})
        private String tel;

        @Schema(description = "비밀번호", allowableValues = {"xxxxx@w4124"})
        private String passwd;
    }


}
