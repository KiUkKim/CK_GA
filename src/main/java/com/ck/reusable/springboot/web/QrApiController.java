package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.user.UserRepository;
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

    private final UserRepository userRepository;

    @PutMapping("/manager/CupStateCheck")
    @ResponseBody
    public QrDto.ForCupStateResponseDto CupStateCheck(@RequestBody QrDto.ForQrResponseDto qrResponseDto, Principal principal)
    {
        Long cupUid = qrResponseDto.getCup_uid();

        String userEmail = principal.getName();

        Integer check = qrService.checkCupStateService(cupUid);

        Integer nowCnt = userService.UserCupNowCnt(userEmail);

        String message = "";

        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척 )
         */
        switch (check){
            case 0:
                if(nowCnt > 2)
                {
                    message = "해당 유저의 대여 가능 컵 개수가 " + nowCnt + "로 초과했습니다.";
                    break;
                }
                else{
                    message = "대여 가능한 컵입니다.";
                    break;
                }
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

        QrDto.ForCupStateResponseDto responseDto = new QrDto.ForCupStateResponseDto();
        responseDto.setCupState(message);
        return responseDto;
    }

    @PutMapping("/manager/CupRental")
    @ResponseBody
    public String cupRentalResponse(@RequestBody QrDto.ForCupRentalResponseDto forCupRentalResponseDto, Principal principal)
    {
        // 매장 직원 메일
        String ManagerEmail = principal.getName();

        Long cupUid = forCupRentalResponseDto.getCupUid();
        Long userUid = forCupRentalResponseDto.getUserUid();;

        String userEmail = userRepository.findEmailByUser_id(userUid);

        // 깔끔한 고객 출력을 원함 -> email -> 이름으로 출력
        String name = userRepository.PrintUserName(userUid);

        // Check Cup State
        Integer check = qrService.checkCupStateService(cupUid);

        // Cup 개수 초과인지 확인
        Integer nowCnt = userService.UserCupNowCnt(userEmail);

        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척 )
         */
        if(check == 0)
        {
            if(nowCnt <= 2)
            {
                userService.UserRental(userEmail, cupUid);
                nowCnt = userService.UserCupNowCnt(userEmail);

                return name + "고객님의 대여가 정상적으로 이루어졌습니다. 현재 대여 컵 개수는 " + nowCnt + "개 입니다.";
            }
            else if(nowCnt > 2)
            {
                nowCnt = userService.UserCupNowCnt(userEmail);

                return name + "고객님의 대여가능 컵 개수는 " + nowCnt + "개로 대여가 불가능합니다.";
            }
        }

        return "반납이 완료된 컵으로 대여가 불가능합니다.";
    }
}
