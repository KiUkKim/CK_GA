package com.ck.reusable.springboot.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.security.PrincipalDetails;
import com.ck.reusable.springboot.web.jwt.JwtProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter 중 BasicAuthenticationFilter 구현
// 권한 인증이 필요한 구간은 해당 필터를 타게 되어있음
// 권한 인증 필요한 구간 x -> 해당 필터 안탐
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    private JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository)
    {
        super(authenticationManager);

        // UserRepository를 연결하기 위해 연동
        this.userRepository = userRepository;
    }

    // 인증 권한 페이지는 해당 필터를 탐
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소가 요청 됨.");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader : " + jwtHeader);


        // Header가 존재하지 않는 값이거나, Bearer token이 아닐경우 ( 잘못된 토큰 값이 들어온 경우) filter로 걸러줌.
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer"))
        {
            chain.doFilter(request, response);
            return;
        }

        // JwtToken 을 검증해서 정상적인 사용자인지 확인
        // Bearer [token값]으로 들어오는데 Bearer와 공백 한칸을 제외한 token값을 가져옴
        String jwtToken = request.getHeader(jwtProperties.HEADER_STRING).replace(jwtProperties.TOKEN_PREFIX, "");

        // Hash방식의 암호화된 곳 사인값으로 확인
        // 올바르게 작동되었다면 token으로 만들 때 사용한 아이디값을 가져오게 된다.
        String email = JWT.require(Algorithm.HMAC512(jwtProperties.SECRET)).build().verify(jwtToken).getClaim( "email").asString();

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
}
