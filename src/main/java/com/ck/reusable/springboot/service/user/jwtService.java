//package com.ck.reusable.springboot.service.user;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.ck.reusable.springboot.domain.user.User;
//import com.ck.reusable.springboot.security.CustomUserDetail;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class jwtService{
//
//    private AuthenticationManager authenticationManager;
//
//    public String getJwtToken(Long member_seq)
//    {
//        System.out.println("토큰 제작시작");
//        return JWT.create().withSubject("jwtToken").withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 1000) ))
//                .withClaim("member_seq", member_seq).sign(Algorithm.HMAC512("kim"));
//    }
//
//    public void setSecuritySession(Authentication authentication)
//    {
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    public Authentication getAuthentication(User user)
//    {
//        CustomUserDetail customUserDetail = new CustomUserDetail(user);
//
//        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), customUserDetail.getAuthorities()));
//    }
//}
