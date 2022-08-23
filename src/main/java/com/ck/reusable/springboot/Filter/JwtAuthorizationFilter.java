package com.ck.reusable.springboot.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ck.reusable.springboot.domain.user.RefreshJwt;
import com.ck.reusable.springboot.domain.user.RefreshJwtRepository;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.security.PrincipalDetails;
import com.ck.reusable.springboot.web.jwt.JwtProperties;
import com.ck.reusable.springboot.web.jwt.jwtCookieUtilService;
import com.ck.reusable.springboot.service.user.jwtService;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// 시큐리티가 filter 중 BasicAuthenticationFilter 구현
// 권한 인증이 필요한 구간은 해당 필터를 타게 되어있음
// 권한 인증 필요한 구간 x -> 해당 필터 안탐
@RestController
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    private final RefreshJwtRepository jwtRepository;

    private final jwtService jwtService;

    private final jwtCookieUtilService jwtCookieUtilService;

    private JwtProperties jwtProperties;

    private String refreshTokenName = "RefreshToken";

    private String accessTokenName = "Authorization";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshJwtRepository jwtRepository,
                                  jwtService jwtService, jwtCookieUtilService jwtCookieUtilService)
    {
        super(authenticationManager);

        this.jwtService = jwtService;
        this.jwtRepository = jwtRepository;
        // UserRepository를 연결하기 위해 연동
        this.userRepository = userRepository;
        this.jwtCookieUtilService = jwtCookieUtilService;
    }



    // 인증 권한 페이지는 해당 필터를 탐
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("doFilterInternal 인증이나 권한이 필요한 주소가 요청 됨.");

        logger.info("요청 url : " + request.getRequestURI());

        String jwtHeader = request.getHeader("Authorization");

        logger.info("jwtHeader : " + jwtHeader);

        // Header가 존재하지 않는 값이거나, Bearer token이 아닐경우 ( 잘못된 토큰 값이 들어온 경우) filter로 걸러줌.
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer"))
        {
            logger.info("헤더값이 존재하지 않음.");
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = "";

        try{
            // JwtToken 을 검증해서 정상적인 사용자인지 확인
            // Bearer [token값]으로 들어오는데 Bearer와 공백 한칸을 제외한 token값을 가져옴
            // 쿠키 값에서 가져오는 방식
            jwtToken = request.getHeader(jwtProperties.HEADER_STRING).replace(jwtProperties.TOKEN_PREFIX, "");

//            jwtToken = jwtCookieUtilService.getCookieValue(request, accessTokenName);

            logger.info("AccessToken 추출 : " + jwtToken);

            // Hash방식의 암호화된 곳 사인값으로 확인
            // 올바르게 작동되었다면 token으로 만들 때 사용한 아이디값을 가져오게 된다.
            String email = JWT.require(Algorithm.HMAC256(jwtProperties.SECRET)).build().verify(jwtToken).getClaim( "email").asString();

            logger.info("email 출력 : " + email);

            // 서명이 정상적으로 된 경우, User 존재가 맞는지 판단.
            // 로그인한 정보로 검증을 하는 것이 아니라, JWT token을 통해서 판단.
            if(email != null)
            {
                User user = userRepository.findUserByEmail(email);

                PrincipalDetails principalDetails = new PrincipalDetails(user);

                // Email이 확실하다는 것은 - 정상적으로 로그인이 수행됐다는 뜻이므로 null [비밀번호 구간 ] 이 가능하다.
                // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 생성해준다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        }
        catch (TokenExpiredException e)
        {
            logger.info("만료된 엑세스 토큰 : " + jwtToken);

            // 리프레쉬 토큰 꺼내기
            String refreshToken = Optional.ofNullable(request.getHeader(refreshTokenName).replace(jwtProperties.TOKEN_PREFIX, "")).orElseGet(()->null);

            logger.info("리프레쉬 토큰 : " + refreshToken);

            logger.info(refreshTokenName + "  " + refreshToken + " user_id 찾기");

            Long id = jwtRepository.findUserId(refreshToken);

//            Long id = jwtService.getRefreshTokenId(refreshToken);

            logger.info("리프레쉬 토큰 id " + id);

            String email = Optional.ofNullable(userRepository.findEmailByUser_id(id)).orElseGet(()->null);

            logger.info("refresh token으로 찾은 이메일 : " + email);

            Timestamp getCreated = jwtRepository.findDateTime(refreshToken);

            // 토큰 유효성 검증
            // 기간 지났으면 재발급
            if(jwtService.checkRefreshTokenValidity(getCreated))
            {
                jwtService.deleteRefreshToken2(refreshToken);
                refreshToken = jwtService.getJwtToken();
                jwtService.insertRefreshToken(refreshToken, id);
                logger.info("새로운 리프레시 토큰 : " + refreshToken);
                response.addHeader(jwtProperties.REFRESH_STRING, jwtProperties.TOKEN_PREFIX + refreshToken);
            }
            // 기간 안지났다면 그대로 진행. (refresh token 그대로 진행)

            if(refreshToken == null || email == null)
            {
                jwtCookieUtilService.goForward("/tokenExpire/X", request, response);
                return;
            }

            // 새 엑서스 토큰 발급 ( 예외 처리 후, 받아오기
            String accessToken= jwtService.get_access_token(email);

            logger.info("새로운 토큰 발급 : " + accessToken);

            Map<String, Object> accessTokenCookie = new HashMap<>();
            accessTokenCookie.put(accessTokenName, accessToken);

            jwtCookieUtilService.TokenToCookie(accessTokenCookie, response);

            jwtCookieUtilService.goForward("/tokenExpire/Y", request, response);
        }catch (NullPointerException e2)
        {
            logger.info("토큰이 없음 비로그인 사용자 요청");
            jwtCookieUtilService.goForward("/tokenExpire/X", request, response);
        }

        logger.info("인증필터 통과");
        chain.doFilter(request, response);
    }

}
