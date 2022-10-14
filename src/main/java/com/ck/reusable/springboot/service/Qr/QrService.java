package com.ck.reusable.springboot.service.Qr;

import com.ck.reusable.springboot.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class QrService {

    private final CupRepository cupRepository;

    private final rentalHistoryRepository rentalHistoryRepository;

    private final UserRepository userRepository;

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
    public void cupReturnService(Long goodAttitudeCup_Uid)
    {
        Long user_id = 1L;

        rentalHistoryRepository.RentalReturnQuery(goodAttitudeCup_Uid);

//        rental_history rh = rentalHistoryRepository.SelectRentalHistory(goodAttitudeCup_Uid);
//        Assert.notNull(rh, "rh is must not be null");
//        rentalHistoryRepository.CupReturnQuery(rh);
        User user = rentalHistoryRepository.SelectUser(goodAttitudeCup_Uid);
        Cup cup = rentalHistoryRepository.SelectCup(goodAttitudeCup_Uid);

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
