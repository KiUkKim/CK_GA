package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.QrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class QrApiController {

    private final UserService userService;



//    @PutMapping("/manager/Qrcheck")
//    @ResponseBody
//    public String CupStateCheck(QrDto.ForQrResponseDto qrResponseDto, Principal principal)
//    {
//        Long cupUid = qrResponseDto.getCup_uid();
//        String userEmail = principal.getName();
//
//
//    }
}
