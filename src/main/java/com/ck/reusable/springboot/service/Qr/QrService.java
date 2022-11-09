package com.ck.reusable.springboot.service.Qr;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.Cup.CupRepository;
import com.ck.reusable.springboot.domain.History.rentalHistoryRepository;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.ck.reusable.springboot.domain.History.returnHistoryRepository;
import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.domain.Store.StoreInfoRepository;
import com.ck.reusable.springboot.domain.user.*;
import com.ck.reusable.springboot.service.user.RentalHistoryService;
import com.ck.reusable.springboot.web.dto.ReturnHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class QrService {

    private final CupRepository cupRepository;

    private final com.ck.reusable.springboot.domain.History.rentalHistoryRepository rentalHistoryRepository;

    private final StoreInfoRepository storeInfoRepository;

    private final UserRepository userRepository;

    private final RentalHistoryService rentalHistoryService;

    // 컵의 상태를 체크하는 부분
    @Transactional
    public Integer checkCupStateService(Long goodAttitudeCup_Uid)
    {
        return cupRepository.CanRental(goodAttitudeCup_Uid);
    }


    /*
    컵 반환 로직
     */
    @Transactional
    public void cupReturnService(Long goodAttitudeCup_Uid, String email)
    {
        /*
        ReturnHistory Logic
         */

        // 매장을 받기 위함 (로그인된 직원)
        Long storeId = userRepository.returnStoreInfo(email);

        // Rental History와 엮기 위함함
        ReturnHistoryDto.ReturnHistoryResponseDto responseDto = new ReturnHistoryDto.ReturnHistoryResponseDto();


        responseDto.setStoreId(storeInfoRepository.ReturnStoreId(storeId));
        responseDto.setReturnAt(LocalDateTime.now());

        System.out.println("GA : " + goodAttitudeCup_Uid);
        responseDto.setRentalId(rentalHistoryRepository.RentalHistoryLogic(goodAttitudeCup_Uid));

        System.out.println(rentalHistoryRepository.RentalHistoryLogic(goodAttitudeCup_Uid));

        rentalHistoryService.saveReturnHistory(responseDto);

        
        // 이외 옵션 지정

        // checkvalue = 1로 변경
        rentalHistoryRepository.RentalReturnQuery(goodAttitudeCup_Uid);

        // Load User & Cup
        // 해당 컵의 User와 Cup을 받아오기
        User user = rentalHistoryRepository.SelectUser(goodAttitudeCup_Uid);
        Cup cup = rentalHistoryRepository.SelectCup(goodAttitudeCup_Uid);

        // user now_cnt 감소
        user.setNow_cnt(user.getNow_cnt() - 1);

        cup.setCupState(2);

    }


    /*
    컵 관련 통합 부분 ( Integer -> String )
     */
    @Transactional
    public String FormatCupState(Integer cupState)
    {
        if(cupState == 0)
        {
            return "available";
        }
        else if(cupState == 1)
        {
            return "using";
        }
        else if(cupState == 2)
        {
            return "returned";
        }
        else if(cupState == 3)
        {
            return "cleanse";
        }
        else if(cupState == 4)
        {
            return "loss";
        }

        return "";
    }
}
