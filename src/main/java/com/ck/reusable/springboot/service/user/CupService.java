package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.Cup.CupRepository;
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
import java.util.List;

@RequiredArgsConstructor
@Service
public class CupService {

    private final CupRepository cupRepository; // 나중에 삭제 시켜주어야 함
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


    //////////////////////////// 나중에 삭제되어야 할 부분 /////////////////////////
    ////////////////////////// 테스트를 위해서 넣어둠 /////////////////////////////
    @Transactional
    public List<Cup> cupInitial()
    {
        return cupRepository.findAllCup();
    }
}
