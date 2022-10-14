package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface rentalHistoryRepository extends JpaRepository<rental_history, Long> {

    /*
    rental 부분 체크 - history 체크 영역
     */

//    @Modifying
//    @Query("update Cup c set c.cupState = 1 WHERE c.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
//    void UpdateCupState(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

    /*
    컵 반환 로직
     */
    //TODO
    // 컵 상태 return 으로 변경 , 유저 상태 now_cnt - 1 , rental_history - return at 현재 시간으로 받기
    @Modifying(clearAutomatically = true)
    @Query("update rental_history rh set rh.returnAT = current_timestamp " +
            "WHERE rh.cup.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    void RentalReturnQuery(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

    @Modifying
    @Query("update Cup c SET c.cupState = 2 where c.rental_histories = :rental_history ")
    void CupReturnQuery(rental_history rental_history);

    @Query("SELECT u FROM User u,rental_history rh where rh.cup.goodAttitudeCup_Uid = :goodAttitudeCup_Uid and u.member_seq = rh.user.member_seq")
    User SelectUser(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

    @Query("SELECT c FROM Cup c, rental_history rh where rh.cup.goodAttitudeCup_Uid = :goodAttitudeCup_Uid " +
            "and c.goodAttitudeCup_Uid = rh.cup.goodAttitudeCup_Uid")
    Cup SelectCup(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);
}
