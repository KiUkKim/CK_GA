package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.Cup.CupRepository;
import com.ck.reusable.springboot.domain.History.rentalHistoryRepository;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.ck.reusable.springboot.domain.Store.StoreInfoRepository;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.service.Qr.QrService;
import com.ck.reusable.springboot.service.user.CupService;
import com.ck.reusable.springboot.service.user.RentalHistoryService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.QrDto;
import com.ck.reusable.springboot.web.dto.RentalHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController
public class QrApiController {

//    ====================== Service

    private final UserService userService;

    private final RentalHistoryService rentalHistoryService;

    private final QrService qrService;

    private final CupService cupService;

//    ===================== Respository

    private final CupRepository cupRepository;

    private final rentalHistoryRepository rentalHistoryRepository;

    private final StoreInfoRepository storeInfoRepository;

    private final UserRepository userRepository;

//    ========================== main

    @PutMapping("/manager/CupStateCheck")
    @ResponseBody
    public QrDto.ForCupStateResponseDto CupStateCheck(@RequestBody QrDto.ForQrResponseDto qrResponseDto, Principal principal, HttpServletRequest request, HttpServletResponse response)
    {
        Long goodAttitudeCup_Uid = qrResponseDto.getGoodAttitudeCup_Uid();
        System.out.println("cup_UID : " + goodAttitudeCup_Uid );

        Integer check = qrService.checkCupStateService(goodAttitudeCup_Uid);

        String message = "";

        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척, 4: 분실, 5: 대여기간 만료 expire)
        // available, using, returned, cleanse, loss, expire
         */
        switch (check){
            case 0:
                    message = "available";
                    break;
            case 1:
                // 자동 반납 기능 처리! ( USER 정보 확인할 필요 X )
                cupService.goReturn("/cupReturn", request, response, qrResponseDto, principal);
                break;
            case 2:
                message = "returned";
                break;
            case 3:
                message = "cleanse";
                break;
            case 4:
                message = "loss";
                break;
            case 5:
                message = "expire";
                break;
        }

        QrDto.ForCupStateResponseDto responseDto = new QrDto.ForCupStateResponseDto();
        responseDto.setCupState(message);
        return responseDto;
    }

    @PutMapping("/manager/CupRental")
    @ResponseBody
    public Object cupRentalResponse(@RequestBody QrDto.ForCupRentalResponseDto forCupRentalResponseDto, Principal principal)
    {
        // 매장 직원 메일
        String ManagerEmail = principal.getName();

        // user를 받아옴
        User user = userService.searchUserByEmail2(principal.getName());

        // DTO 입력 부분
        Long cupUid = forCupRentalResponseDto.getGoodAttitudeCup_Uid();

        Long userUid = forCupRentalResponseDto.getUserUid();

        String userEmail = userRepository.findEmailByUser_id(userUid);
        // 깔끔한 고객 출력을 원함 -> email -> 이름으로 출력
//        String name = userRepository.PrintUserName(userUid);

        // Check Cup State
        Integer check = qrService.checkCupStateService(cupUid);

        // Cup 개수 초과인지 확인
        Integer nowCnt = user.getNow_cnt();

        String msg = qrService.FormatCupState(check);

        // 컵 상태 반환 - cupstate dto 형식으로 -- 2022 10/01 완료
        QrDto.ForCupStateResponseDto responseMessageDto = new QrDto.ForCupStateResponseDto();
        String message = "";

        // 현재 고객이 벤 유저라면 대여 불가능
        if(user.getBanUser())
        {
            msg = "사용할 수 없는 계정입니다.";

            message = msg;
            responseMessageDto.setCupState(message);

            return new ResponseEntity<>(responseMessageDto, HttpStatus.FORBIDDEN);
        }


        /*
        cupState 에 따른 구분 ( 0 : 대여가능, 1 : 대여중, 2: 반납 , 3: 세척, 4: 분실, 5: 만료 )
         */
        if(check == 0)
        {
            if(nowCnt < 2)
            {
                userService.UserRental(userEmail, cupUid);

                Cup cup = cupRepository.cupReturn(cupUid);

                cup.setCupTotalCnt(cup.getCupTotalCnt() + 1);

                // user_rental history 연결

//                nowCnt = userService.UserCupNowCnt(userEmail);

                /*
                // Rental history Logic
                 */
                LocalDateTime time = LocalDateTime.now();

                RentalHistoryDto.RentalHistoryResponseDto responseDto = new RentalHistoryDto.RentalHistoryResponseDto();
                responseDto.setRentalAt(time);
                responseDto.setReturnAT(time.plusMinutes(5));

                responseDto.setUserUid(userService.findUser(userEmail));

                responseDto.setGoodAttitudeCup_Uid(cupRepository.cupReturn(cupUid));

                Long storeId = userRepository.returnStoreInfo(ManagerEmail);

                responseDto.setStoreId(storeInfoRepository.ReturnStoreId(storeId));

                responseDto.setCheckValue(0);

                rentalHistoryService.saveRentalHistory(responseDto);

//                message = name + "고객님의 대여가 정상적으로 이루어졌습니다. 현재 대여 컵 개수는 " + nowCnt + "개 입니다.";
                message = "using";
                responseMessageDto.setCupState(message);
                return new ResponseEntity<>(responseMessageDto, HttpStatus.OK);
            }
            else if(nowCnt >= 2)
            {
                nowCnt = userService.UserCupNowCnt(userEmail);

//                message = name + "고객님의 대여가능 컵 개수는 " + nowCnt + "개로 대여가 불가능합니다.";
                msg = qrService.FormatCupState(check);

                message = msg;
                responseMessageDto.setCupState(message);

                return new ResponseEntity<>(responseMessageDto, HttpStatus.NOT_ACCEPTABLE);
            }
        }

        message = msg;
        responseMessageDto.setCupState(message);
        return responseMessageDto;
    }



    /*
    대여중인 컵일 경우, 바로 반납 처리 부분
     */
        /*
    반납 API
     */


    // 반납 될 때 해당 rental history checkvalue, returnat 수정해주기!

    // Servelet request Body 가져오는 법 공부!
    // user 현재 cnt - 1 , 컵 상태 - return 으로 반환
    @PutMapping("/cupReturn")
    @ResponseBody
    public QrDto.ForCupStateResponseDto CupReturnAPI(HttpServletRequest request, HttpServletResponse response)
    {
        String msgBody = "";

        // Load CupId From Servlet
        Object cupId = request.getAttribute("goodAttitudeCupUid");

        Long GoodAttitudeCup_Uid = Long.valueOf(String.valueOf(cupId));

        // Load GoodAttitudeCUp_Uid Form Servlet
        Object Memail = request.getAttribute("mEmail");

        String ManagerEmail = String.valueOf(String.valueOf(Memail));

        // Print Result For Checking Success Result
        System.out.println("cupState " +  GoodAttitudeCup_Uid);

        System.out.println("Manager Email : " + ManagerEmail);



        //컵 반환
        qrService.cupReturnService(GoodAttitudeCup_Uid, ManagerEmail);

        // 컵 반환 후 해당 유저의 락 해제 여부 체크
        qrService.unLockUserService(GoodAttitudeCup_Uid);

        // 컵 반환 후 상태 체크
        Integer check = qrService.checkCupStateService(GoodAttitudeCup_Uid);

        QrDto.ForCupStateResponseDto cupStateResponseDto = new QrDto.ForCupStateResponseDto();

        // 정상적으로 반납이 이루어졌으면 2
        if(check == 2)
        {
            cupStateResponseDto.setCupState("returning");
            return cupStateResponseDto;
        }

        cupStateResponseDto.setCupState("fail");

        return cupStateResponseDto;
    }

    // 컵 분실신고
    @PutMapping("/user/cupLost")
    @ResponseBody
    public Object cupLostController(@RequestBody QrDto.ForCupLostResponseDto cupLostResponseDto, Principal principal)
    {
        // 매장 직원 메일
        String ManagerEmail = principal.getName();

        // user를 받아옴
        User user = userService.searchUserByEmail2(principal.getName());

        // DTO 입력 부분
        Long cupUid = cupLostResponseDto.getGoodAttitudeCup_Uid();

        // Check Cup State
        Integer check = qrService.checkCupStateService(cupUid);
        rental_history rental_history= rentalHistoryService.RentalHistoryObject(cupUid);
        String msg = "";

        // 컵이 현재 대여중이고 해당 컵을 빌린 유저와 현재 분실 처리하려는 유저가 일치하는지 체크
        if(check == 1 && rental_history.getUser().getMember_seq() == user.getMember_seq())
        {
            // 바꿔줘야하는 부분은 다음과 같습니다
            // 컵 상태 ->  4로 변경
            // 현재 대여중인 유저의 빌리고 있는 컵 1개 줄임
            // rental history -> 3번으로 변경 ( 2번이 현재 임시로 사용중이기 때문에 3번으로 사용 )

            // 컵 상태 분실 처리
            rentalHistoryService.CupLostService(principal.getName(), cupUid);

            msg = cupUid + "번 컵의 분실처리가 정상적으로 이루어졌습니다.";

            return new ResponseEntity<>(msg, HttpStatus.OK);
        }

        msg = cupUid + "번 컵의 분실처리가 정상적으로 이루어지지 않았습니다.";

        return new ResponseEntity<>(msg, HttpStatus.NOT_ACCEPTABLE);


    }

}
