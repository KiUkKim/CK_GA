package com.ck.reusable.springboot.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.security.PrincipalDetails;
import com.ck.reusable.springboot.web.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Authenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class jwtService{

    public AuthenticationManager authenticationManager;

    private JwtProperties jwtProperties;

    public String getJwtToken(Long member_seq, String email)
    {
        System.out.println("토큰 제작시작");

        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.SECRET);

        return JWT.create()
                .withSubject("CkReusableCup")
                .withIssuer(jwtProperties.TOKEN_ISSUR) // 토큰 유효시간 30분
                .withExpiresAt(new Date(System.currentTimeMillis() + (jwtProperties.EXPIRATION_TIME)))
                .withClaim("id", member_seq)
                .withClaim("email", email)
                .sign(algorithm);
    }

    public void setSecuritySession(Authentication authentication)
    {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Authentication getAuthentication(User user)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }
}
