package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.user.CupRepository;
import com.ck.reusable.springboot.domain.user.StoreInfoRepository;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.service.Qr.QrService;
import com.ck.reusable.springboot.service.user.RentalHistoryService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.QrDto;
import com.ck.reusable.springboot.web.dto.RentalHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class QrApiController {

    private final UserService userService;

    private final RentalHistoryService rentalHistoryService;

    private final QrService qrService;

    private final UserRepository userRepository;

    private final StoreInfoRepository storeInfoRepository;

    private final CupRepository cupRepository;

    @PutMapping("/manager/CupStateCheck")
    @ResponseBody
    public QrDto.ForCupStateResponseDto CupStateCheck(@RequestBody QrDto.ForQrResponseDto qrResponseDto, Principal principal)
    {
        Long goodAttitudeCup_Uid = qrResponseDto.getGoodAttitudeCup_Uid();
        System.out.println("cup_UID : " + goodAttitudeCup_Uid );
        String userEmail = principal.getName();

        Integer check = qrService.checkCupStateService(goodAttitudeCup_Uid);

        Integer nowCnt = userService.UserCupNowCnt(userEmail);

        String message = "";

        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척 )
         */
        switch (check){
            case 0:
                if(nowCnt > 1)
                {
                    message = "해당 유저의 대여 가능 컵 개수가 " + nowCnt + "로 초과했습니다.";
                    break;
                }
                else{
                    message = "대여 가능한 컵입니다.";
                    break;
                }
            case 1:
                //TODO
                // 자동 반납 기능 처리! ( USER 정보 확인할 필요 X )

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
    public QrDto.ForCupStateResponseDto cupRentalResponse(@RequestBody QrDto.ForCupRentalResponseDto forCupRentalResponseDto, Principal principal)
    {
        // 매장 직원 메일
        String ManagerEmail = principal.getName();

        Long cupUid = forCupRentalResponseDto.getGoodAttitudeCup_Uid();
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

        //TODO
        // 컵 상태 반환 - cupstate dto 형식으로 -- 2022 10/01 완료
        QrDto.ForCupStateResponseDto responseMessageDto = new QrDto.ForCupStateResponseDto();
        String message = "";

        if(check == 0)
        {
            if(nowCnt < 2)
            {
                userService.UserRental(userEmail, cupUid);
                // user_rental history 연결


                nowCnt = userService.UserCupNowCnt(userEmail);


                /*
                // Rental history Logic
                 */
                RentalHistoryDto.RentalHistoryResponseDto responseDto = new RentalHistoryDto.RentalHistoryResponseDto();
                responseDto.setRentalAt(LocalDateTime.now());
                responseDto.setUserUid(userService.findUser(userEmail));

                responseDto.setGoodAttitudeCup_Uid(cupRepository.cupReturn(cupUid));

                Long storeId = userRepository.returnStoreInfo(ManagerEmail);

                responseDto.setStoreId(storeInfoRepository.ReturnStoreId(storeId));

                responseDto.setCheckValue(0);

                rentalHistoryService.saveRentalHistory(responseDto);

                message = name + "고객님의 대여가 정상적으로 이루어졌습니다. 현재 대여 컵 개수는 " + nowCnt + "개 입니다.";
                responseMessageDto.setCupState(message);
                return  responseMessageDto;
            }
            else if(nowCnt >= 2)
            {
                nowCnt = userService.UserCupNowCnt(userEmail);

                message = name + "고객님의 대여가능 컵 개수는 " + nowCnt + "개로 대여가 불가능합니다.";
                responseMessageDto.setCupState(message);

                return responseMessageDto;
            }
        }

        message = "반납이 완료된 컵으로 대여가 불가능합니다.";
        responseMessageDto.setCupState(message);
        return responseMessageDto;
    }



    /*
    대여중인 컵일 경우, 바로 반납 처리 부분
     */

}
