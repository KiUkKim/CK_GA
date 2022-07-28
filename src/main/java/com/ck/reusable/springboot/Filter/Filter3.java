package com.ck.reusable.springboot.Filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Filter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // POST METHOD에서 인증 유효
        if(req.getMethod().equals("POST"))
        {
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("cos"))
            {
                chain.doFilter(req, res);
            }
            else{
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }

    }
}
