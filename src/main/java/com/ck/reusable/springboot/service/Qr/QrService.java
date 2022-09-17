package com.ck.reusable.springboot.service.Qr;

import com.ck.reusable.springboot.domain.user.CupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QrService {

    private final CupRepository cupRepository;

    //TODO
    // 컵의 상태를 체크하는 부분
    @Transactional
    public Integer checkCupStateService(Long cupUid)
    {
        return cupRepository.CanRental(cupUid);
    }

    //TODO
    // 컵 대여 부분
    // 생각해야 하는 부문 : user의 now_cnt 증가, total_cnt 증가 - 해당 유저와 rental history 연결


}
