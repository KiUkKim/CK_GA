//package com.ck.reusable.springboot.web.jwt;
//
//
//import com.ck.reusable.springboot.domain.user.User;
//import com.ck.reusable.springboot.security.CustomUserDetail;
//import com.ck.reusable.springboot.service.user.jwtService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class jwtLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private jwtService jwtService;
//
//    public jwtLoginFilter(jwtService jwtService){
//        this.jwtService=jwtService;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
//        System.out.println("로그인 요청 attemptAuthentication ");
//
//        try {
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            User user = objectMapper.readValue(request.getInputStream(), User.class);
//
//            System.out.println(user);
//
//            Authentication authentication = jwtService.getAuthentication(user);
//
//            jwtService.setSecuritySession(authentication);
//
//            System.out.println("로그인 완료" + authentication.getName());
//
//            return authentication;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws AuthenticationException{
//        System.out.println("토큰 제작 시작");
//
//        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
//
//        String jwtToken = jwtService.getJwtToken(customUserDetail.getUser().getMember_seq());
//
//        response.setHeader("Authorization", "Bearer " +jwtToken);
//    }
//}
