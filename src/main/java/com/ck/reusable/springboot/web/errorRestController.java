package com.ck.reusable.springboot.web;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage4;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage5;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage6;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage7;
import com.ck.reusable.springboot.domain.user.RefreshJwt;
import com.ck.reusable.springboot.domain.user.RefreshJwtRepository;
import com.ck.reusable.springboot.service.user.jwtService;
import com.ck.reusable.springboot.web.jwt.JwtProperties;
import com.ck.reusable.springboot.web.jwt.jwtCookieUtilService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class errorRestController {
    private final jwtCookieUtilService jwtCookieUtilService;

    private JwtProperties jwtProperties;

    private final jwtService jwtService;

    private final RefreshJwtRepository jwtRepository;

    @RequestMapping("/tokenExpire/Y")
    public void name(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("/tokenExpire/Y");
        throw new TokenExpiredException(null, null);
    }

    // RefreshToken 이 안담겨 있을 경우 처리
    @RequestMapping("/refreshTokenValidation/X")
    public Object refreshTokenValidation(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("/refreshTokenValidation/X");

        String refreshToken = request.getHeader("RefreshToken");

        String accessToken = request.getHeader("Authorization");

        errorMessage6 er6 = errorMessage6.builder().accessToken(accessToken).refreshToken(refreshToken).status("401")
                .message("AccessToken이 만료되었습니다. RefreshToken을 이용해서 다시 요청하세요")
                .url("/refreshTokenValidation/X").build();

        return new ResponseEntity<>(er6, HttpStatus.UNAUTHORIZED);
    }



    //TODO
    // RefreshToken 만료에 대한 처리
    @RequestMapping("/RefreshTokenExpire/Y")
    public Object RefreshTokenExpire(HttpServletRequest request, HttpServletResponse response)
    {
        String refreshToken = request.getHeader("RefreshToken");
        System.out.println(refreshToken);
        errorMessage7 er7 = errorMessage7.builder().refreshToken(refreshToken)
                .message("해당 RefreshToken 만료되었습니다. 다시 로그인해주세요.").status("401")
                .url("/RefreshTokenExpire/Y").build();

        return new ResponseEntity<>(er7, HttpStatus.UNAUTHORIZED);
    }


    @RequestMapping("/tokenExpire/X")
    public Object error(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("/tokenExpire/X");

        String accessToken = request.getHeader("Authorization");

        String refreshToken = request.getHeader("RefreshToken");

        System.out.println(accessToken);

        errorMessage4 er4 = errorMessage4.builder().message("토큰의 값이 유효하지 않습니다.").status("401").
                refreshToken(accessToken).accessToken(refreshToken).build();

        return new ResponseEntity<>(er4, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public JSONObject TokenExpiredException(TokenExpiredException exception, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("TokenExpiredException 토큰 만료");
        String refreshToken = request.getHeader("RefreshToken");
        System.out.println(refreshToken + " 리프레시 토큰");

        if (refreshToken.startsWith("Bearer")) {
            refreshToken = refreshToken.replace(jwtProperties.TOKEN_PREFIX, "");

            String jwtToken = request.getHeader(jwtProperties.HEADER_STRING).replace(jwtProperties.TOKEN_PREFIX, "");

//            RefreshJwt refreshJwt = jwtService.getRefreshToken(refreshToken);

            System.out.println(refreshToken + " 새로운 토큰 발급!");

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Authorization", jwtToken);
            jsonObject.put("RefreshToken",  refreshToken);
            jsonObject.put("message" , "해당 엑서스 토큰이 만료되었습니다. 토큰 재발급을 받아주세요");

//            response.addHeader("Authorization", refreshJwt);

            return jsonObject;
        }

        return null;
    }

    @PutMapping("/refreshTokenRenew")
    public JSONObject TokenRenewPage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("TokenExpiredException 토큰 재발급시작");

        String refreshToken = request.getHeader("RefreshToken");

        System.out.println(refreshToken + " 리프레시 토큰");

        if (refreshToken.startsWith("Bearer")) {
            refreshToken = refreshToken.replace(jwtProperties.TOKEN_PREFIX, "");

            Timestamp getCreated = jwtRepository.findDateTime(refreshToken);

            if(jwtService.checkRefreshTokenValidity(getCreated))
            {
                System.out.println("================================리프레시 토큰 만료");
                System.out.println("요청 url : /RefreshTokenExpire/Y");
                jwtCookieUtilService.goForward("/RefreshTokenExpire/Y", request, response);
                return null;
            }

            RefreshJwt refreshJwt = jwtService.getRefreshToken(refreshToken);

            String newJwtToken = jwtService.NewJwtToken(refreshJwt);

            System.out.println(newJwtToken + " 새로운 토큰 발급!");

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Authorization", newJwtToken);
            jsonObject.put("RefreshToken",  refreshToken);
            jsonObject.put("message" , "엑서스 토큰이 정상적으로 재발급 되었습니다.");

            response.addHeader("Authorization", newJwtToken);

            return jsonObject;
        }

        else{
            System.out.println("토큰이 없음 비로그인 사용자 요청");
            jwtCookieUtilService.goForward("/tokenExpire/X", request, response);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "올바르지 않은 토큰 값");

            return jsonObject;
        }
    }

}
