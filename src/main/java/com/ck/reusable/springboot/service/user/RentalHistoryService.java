package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.user.rentalHistoryRepository;
import com.ck.reusable.springboot.web.dto.RentalHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class RentalHistoryService {
    private final rentalHistoryRepository rentalRepository;

    @Transactional
    public void saveRentalHistory(RentalHistoryDto.RentalHistoryResponseDto responseDto)
    {
        rentalRepository.save(responseDto.toEntity());
    }
}
