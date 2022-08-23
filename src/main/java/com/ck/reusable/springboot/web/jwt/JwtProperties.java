package com.ck.reusable.springboot.web.jwt;

public interface JwtProperties {
    String SECRET = "CKREUSABLECUP";
//    int EXPIRATION_TIME = 600000 * 12; //( 10분 * 12 = 2시간);
    int EXPIRATION_TIME = 60000 * 2; //( 테스트 맵핑 1분);
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_STRING = "RefreshToken";
    String TOKEN_ISSUR = "https://ck-reusableapp.herokuapp.com/";
//    int EXPIRATION_REFRESH_TIME = 600000 * 6  * 24 * 14;//(10분 * 6 * 24 = 1440분 하루 * 14 ==> 14일)
    int EXPIRATION_REFRESH_TIME = 60000 * 5;//(1분 *  = 300분 하루 - 60분 * 12 = 720분)
}