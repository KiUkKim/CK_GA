package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface rentalHistoryRepository extends JpaRepository<rental_history, Long> {

    /*
    rental 부분 체크 - history 체크 영역
     */

}
