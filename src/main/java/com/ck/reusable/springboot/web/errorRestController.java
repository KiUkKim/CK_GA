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
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class errorRestController {

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

        errorMessage6 er6 = errorMessage6.builder().accessToken(accessToken).refreshToken(refreshToken).status("403")
                .message("AccessToken이 만료되었습니다. RefreshToken을 이욯해서 다시 요청하세요")
                .url("/refreshTokenValidation/X").build();

        return new ResponseEntity<>(er6, HttpStatus.FORBIDDEN);
    }



    //TODO
    // RefreshToken 만료에 대한 처리
    @RequestMapping("/RefreshTokenExpire/Y")
    public Object RefreshTokenExpire(HttpServletRequest request, HttpServletResponse response)
    {
        String refreshToken = request.getHeader("RefreshToken");

        errorMessage7 er7 = errorMessage7.builder().refreshToken(refreshToken)
                .message("해당 RefreshToken 만료되었습니다. 다시 로그인해주세요.").status("403")
                .url("/RefreshTokenExpire/Y").build();

        return new ResponseEntity<>(er7, HttpStatus.FORBIDDEN);
    }


    @RequestMapping("/tokenExpire/X")
    public Object error(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("/tokenExpire/X");

        String accessToken = request.getHeader("Authorization");

        String refreshToken = request.getHeader("RefreshToken");

        System.out.println(accessToken);

        errorMessage4 er4 = errorMessage4.builder().message("토큰의 값이 유효하지 않습니다.").status("403").
                refreshToken(accessToken).accessToken(refreshToken).build();

        return new ResponseEntity<>(er4, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public JSONObject TokenExpiredException(TokenExpiredException exception, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("TokenExpiredException 토큰 재발급시작");
        String refreshToken = request.getHeader("RefreshToken");
        System.out.println(refreshToken + " 리프레시 토큰");

        if (refreshToken.startsWith("Bearer")) {
            refreshToken = refreshToken.replace(jwtProperties.TOKEN_PREFIX, "");

            RefreshJwt refreshJwt = jwtService.getRefreshToken(refreshToken);

            String newJwtToken = jwtService.NewJwtToken(refreshJwt);

            System.out.println(newJwtToken + " 새로운 토큰 발급!");

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Authorization", newJwtToken);
            jsonObject.put("RefreshToken",  refreshToken);
            jsonObject.put("message" , "액세스토큰 값이 업데이트 되었습니다.");

            response.addHeader("Authorization", newJwtToken);

            return jsonObject;
        }

        return null;
    }

}
