package com.ck.reusable.springboot.security;

import com.ck.reusable.springboot.Filter.JwtAuthenticationFilter;
import com.ck.reusable.springboot.Filter.JwtAuthorizationFilter;
import com.ck.reusable.springboot.domain.user.RefreshJwtRepository;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.web.jwt.jwtCookieUtilService;
import com.ck.reusable.springboot.service.user.jwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;

    private final UserRepository userRepository;

    private final jwtCookieUtilService jwtCookieUtilService;

    private final RefreshJwtRepository jwtRepository;

    private final jwtService jwtService;


    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable(); // csrf 보안 토큰 disable처리.
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용x.
                .and()
                .formLogin().disable()
                .httpBasic().disable()// rest api 만을 고려하여 기본 설정은 해제.
                        .addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService)) // crossorigin(인증 필요x ) , 시큐리티 필터에 인증이 필요할 때 등록
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtRepository, jwtService, jwtCookieUtilService)) // Authorization Filter
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/","/auth/**","/login", "/save", "/emailValidate", "/phoneCheck", "/refreshTokenRenew", "/testPage.html", "/**", "/v1/**," +
                        "/api/save")////이 링크들은
                .permitAll()///허용한다
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                .authenticated();
//                .permitAll();
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }
}
