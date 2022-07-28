package com.ck.reusable.springboot.web.jwt;

public interface JwtProperties {
    String SECRET = "CKREUSABLECUP";
    int EXPIRATION_TIME = 60000 * 10; //( 10분);
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}