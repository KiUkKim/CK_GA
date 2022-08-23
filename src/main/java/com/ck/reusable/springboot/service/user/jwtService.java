package com.ck.reusable.springboot.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ck.reusable.springboot.domain.user.RefreshJwt;
import com.ck.reusable.springboot.domain.user.RefreshJwtRepository;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.web.jwt.JwtProperties;
import com.ck.reusable.springboot.web.jwt.jwtCookieUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class jwtService{

    private JwtProperties jwtProperties;

    private final RefreshJwtRepository refreshJwtRepository;

    private final jwtCookieUtilService cookieUtilService;

    private final UserRepository userRepository;

    public void insertRefreshToken(String refreshToken, Long userid)
    {
        try{
            RefreshJwt refreshJwt = new RefreshJwt(0L, refreshToken, userid, null);
            refreshJwtRepository.save(refreshJwt);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean checkRefreshTokenValidity(Timestamp refreshTokenDate)
    {
        return cookieUtilService.checkDate(refreshTokenDate);
    }

    private void deleteRefreshToken(RefreshJwt refreshJwt)
    {
        System.out.println("기한 만료로 deleteRefreshToken 함수 실행");
        try {
            refreshJwtRepository.delete(refreshJwt);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteRefreshToken2(String refreshJwt)
    {
        System.out.println("기한 만료로 deleteRefreshToken2 함수 실행");
        try {
            refreshJwtRepository.deleteToken(refreshJwt);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getJwtToken(Long member_seq, String email)
    {
        System.out.println("1===============================토큰 제작시작");

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.SECRET);

        return JWT.create()
                .withSubject("tokenTest")
                .withIssuer(jwtProperties.TOKEN_ISSUR) // 토큰 유효시간 30분
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.EXPIRATION_TIME)))
                .withClaim("id", member_seq)
                .withClaim("email", email)
                .sign(algorithm);
    }

    public String NewJwtToken(RefreshJwt refreshToken)
    {
        System.out.println("1===============================토큰 재발급 시작");

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.SECRET);

        String email = userRepository.findEmailByUser_id(refreshToken.getUser_id());
        Long member_seq = refreshToken.getUser_id();

        return JWT.create()
                .withSubject("tokenTest")
                .withIssuer(jwtProperties.TOKEN_ISSUR) // 토큰 유효시간 30분
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.EXPIRATION_TIME)))
                .withClaim("id", member_seq)
                .withClaim("email", email)
                .sign(algorithm);
    }

    public String getJwtToken()
    {
        System.out.println("2==============================리프레쉬 토큰 제작 시작");

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.SECRET);

        // 리프레시 토큰 제작
        return JWT.create()
                .withSubject("tokenTest")
                .withIssuer(jwtProperties.TOKEN_ISSUR)
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.EXPIRATION_REFRESH_TIME)))
                .sign(algorithm);

    }

    public RefreshJwt getRefreshToken(Long user_id)
    {
        System.out.println(user_id + "기존 getRefreshToken 찾기");
        return refreshJwtRepository.findForUser(user_id);
    }

    public String getRefreshTokenId(Long user_id)
    {
        System.out.println(user_id + "기존 getRefreshToken 찾기");
        return refreshJwtRepository.findForUserToken(user_id);
    }

    public RefreshJwt getRefreshToken(String TokenName)
    {
        System.out.println(TokenName + "기존 getRefreshToken 찾기");
        return refreshJwtRepository.findByTokenName(TokenName);
    }

    @Transactional
    public Long getRefreshTokenId(String tokenName)
    {
        System.out.println(tokenName + "user_id 찾기");

        Long id = refreshJwtRepository.findUserId(tokenName);
        return id;
    }

    public String get_access_token(String email)
    {
        System.out.println("get_access_token");
        System.out.println("토큰 발급 이메일 :  " + email);

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.SECRET);

        return JWT.create()
                .withSubject("tokenTest")
                .withIssuer(jwtProperties.TOKEN_ISSUR) // 토큰 유효시간 30분
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.EXPIRATION_TIME)))
                .withClaim("email", email)
                .sign(algorithm);
    }


    /*
    getRefreshToken 만료일 지정
     */
    public String getRefreshToken(RefreshJwt refreshJwt, Long user_id)
    {
        String refreshToken = "";

        // 첫 로그인 시 진행
        if(refreshJwt == null)
        {
            refreshToken = getJwtToken();
            insertRefreshToken(refreshToken, user_id);
        }

        // 첫 로그인이 아니라면 기간 확인
        // 기간이 지났다면 이전 토큰 지우고 다시 생성
        else{
            if(checkRefreshTokenValidity(refreshJwt.getCreated()))
            {
                refreshToken = getJwtToken();
                deleteRefreshToken(refreshJwt);
                insertRefreshToken(refreshToken, user_id);
            }
            // 기간 안지났다면 그대로 진행.
            else{
                refreshToken = refreshJwt.getTokenName();
            }
        }
        return refreshToken;
    }


    // 토큰 값 가져오기
    public String getHeaderToken(HttpServletRequest request, String tokenName)
    {
        return  request.getHeader(tokenName).replace(jwtProperties.TOKEN_PREFIX, "");
    }



}
