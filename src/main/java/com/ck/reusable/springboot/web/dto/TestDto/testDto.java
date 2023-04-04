package com.ck.reusable.springboot.web.dto.TestDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class testDto {

    @Getter
    @AllArgsConstructor
    @Data
    public static class ForTokenResponseDto{
        private String token;
    }
}
