package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.web.dto.QrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@Service
public class CupService {

    /*
    자동 반환 관련 페이지 전환
     */
    @Transactional
    public Object goReturn(String uri_direct, HttpServletRequest request, HttpServletResponse response, QrDto.ForQrResponseDto responseDto, Principal principal)
    {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(uri_direct);

        try{
            request.setCharacterEncoding("UTF-8");

            request.setAttribute("goodAttitudeCupUid", responseDto.getGoodAttitudeCup_Uid());
            request.setAttribute("mEmail", principal.getName());
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
