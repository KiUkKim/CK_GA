package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.Cup.CupRepository;
import com.ck.reusable.springboot.domain.History.rentalHistoryRepository;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.ck.reusable.springboot.domain.History.returnHistoryRepository;
import com.ck.reusable.springboot.domain.History.return_history;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.web.dto.RentalHistoryDto;
import com.ck.reusable.springboot.web.dto.ReturnHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RentalHistoryService {
    private final rentalHistoryRepository rentalRepository;
    private final CupRepository cupRepository;
    private final UserRepository userRepository;
    private final returnHistoryRepository returnRepository;

    @Transactional
    public void saveRentalHistory(RentalHistoryDto.RentalHistoryResponseDto responseDto)
    {
        rentalRepository.save(responseDto.toEntity());
    }

    @Transactional
    public void saveReturnHistory(ReturnHistoryDto.ReturnHistoryResponseDto responseDto)
    {
        returnRepository.save(responseDto.toEntity());
    }

    @Transactional
    public rental_history RentalHistoryObject(Long goodAttitudeCup_Uid)
    {
        return rentalRepository.RentalHistoryLogic(goodAttitudeCup_Uid);
    }

    // 현재 대여기록 정보
    @Transactional
    public List<Map<String, Object>> InfoNowRentalHistory(Long user_id)
    {
        return rentalRepository.rhNowHistory(user_id);
    }

    // 과거 대여기록 정보
    @Transactional
    public List<Map<String, Object>> InfoPastRentalHistory(Long user_id, Pageable pageable)
    {
        return rentalRepository.rhPastHistory(user_id, pageable);
    }

    @Transactional
    public List<rental_history> CheckDateService(Long user_id)
    {
        return rentalRepository.CheckCupRentalTime(user_id);
    }

    // 분실된 컵인지 정상적인 컵인지 따지기 위함
    @Transactional
    public Integer CheckLostCup(Long goodAttitudeCup_Uid, String email)
    {
        return rentalRepository.CheckLostCup(goodAttitudeCup_Uid, email);
    }


    // 현재 미반납 된 컵 개수를 따지기 위함
    @Transactional
    Integer CupReturnCountService(Long user_id)
    {
        return rentalRepository.CheckUnReturnCup(user_id);
    }


    // 컵 분실 부분 일괄처리
    @Transactional
    public void CupLostService(String email, Long goodAttitudeCup_Uid)
    {
        cupRepository.UpdateCupLostState(goodAttitudeCup_Uid);
        userRepository.updateReturnUserCnt(email);
        rentalRepository.updateLostRH(goodAttitudeCup_Uid);
    }

    ////////////////////// 삭제되어야 할 부분 ////////////////////////////
    ///////////////////// 테스트 용임 ///////////////////////////////////
    @Transactional
    public void checkZeroCup()
    {
        rentalRepository.findAllZeroCup();
    }

}
