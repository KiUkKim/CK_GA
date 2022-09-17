package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.service.Qr.QrService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.QrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class QrApiController {

    private final UserService userService;

    private final QrService qrService;


    @PutMapping("/manager/Qrcheck")
    @ResponseBody
    public String CupStateCheck(@RequestBody QrDto.ForQrResponseDto qrResponseDto, Principal principal)
    {
        Long cupUid = qrResponseDto.getCup_uid();

        System.out.println("cupUid - " + cupUid);
        String userEmail = principal.getName();

        Integer check = qrService.checkCupStateService(cupUid);
        System.out.println("check - "  + check);
        String message = "";
        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척 )
         */
        switch (check){
            case 0:
                message = "대여 가능한 컵입니다.";
                break;
            case 1:
                message = "대여가 불가능합니다. [ 대여중인 컵  ] ";
                break;
            case 2:
                message = "대여가 불가능합니다. [ 반납처리 된 컵] ";
                break;
            case 3:
                message = "대여가 불가능합니다. [ 세척 중인 컵 ] ";
                break;
        }

        return message;
    }
}
