package com.ck.reusable.springboot.web.jwt;

import org.slf4j.Logger;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class jwtCookieUtilService {

    public static void TokenToCookie(Map<String, Object> infor, HttpServletResponse response)
    {
        System.out.println("쿠키 제작 시작");
        System.out.println("쿠키 내용 : " + infor.toString());

        for(Map.Entry<String, Object> key:infor.entrySet()){
            ResponseCookie cookie = ResponseCookie.from(key.getKey(), key.getValue().toString())
                    .sameSite("None")
                    .secure(true)
                    .maxAge(Math.toIntExact(600000 * 12))
                    .path("/")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString() + ";HttpOnly");
        }
    }

    public boolean checkDate(Timestamp timestamp)
    {
        System.out.println(timestamp + "토큰 기간");
        System.out.println("날짜 비교 시작");

        // Local 시간으로 변환
        LocalDateTime timestamp2 = timestamp.toLocalDateTime();

        // 오늘 날짜에 토큰 기한 계산
//        timestamp2 = timestamp2.plusDays(14);
//        timestamp2 = timestamp2.plusMinutes(5);
        timestamp2 = timestamp2.plusDays(1);

        // 오늘 날짜 계산
        LocalDateTime today = LocalDateTime.now();

        if(timestamp2.isBefore(today))
        {
            System.out.println("토큰 기한 만료");
            return true;
        }
        return false;
    }


    public String getCookieValue(HttpServletRequest request, String Token_name)
    {
        Cookie cookie = WebUtils.getCookie(request, Token_name);

        String cookieValue = cookie.getValue();

        return cookieValue;
    }

    public Object goForward(String uri_direct, HttpServletRequest request, HttpServletResponse response)
    {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(uri_direct);

        try{
            requestDispatcher.forward(request, response);
        }catch (ServletException e)
        {
            e.printStackTrace();
        }catch ( IOException e2)
        {
            e2.printStackTrace();
        }
        return null;
    }

}

