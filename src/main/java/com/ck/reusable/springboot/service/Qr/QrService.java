package com.ck.reusable.springboot.service.Qr;

import com.ck.reusable.springboot.domain.user.CupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QrService {

    private final CupRepository cupRepository;

    // 컵의 상태를 체크하는 부분
    @Transactional
    public Integer checkCupStateService(Long cupUid)
    {
        return cupRepository.CanRental(cupUid);
    }
}
