package com.ck.reusable.springboot.domain.History;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    // 컵 상태 return 으로 변경 , 유저 상태 now_cnt - 1 , rental_history - return at 현재 시간으로 받기
    @Modifying(clearAutomatically = true)
    @Query("update rental_history rh set rh.checkValue = 1" +
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

    // 현재 대여 정보관련 쿼리
    // 쿼리 수정
    @Query("SELECT st.title AS rentalStore, rh.rentalAT AS rentalAt FROM rental_history rh Inner JOIN StoreInfo st ON rh.rentalStore.storeId = st.storeId AND rh.user.member_seq = :user_id " +
            "AND rh.checkValue = 0")
    List<Map<String, Object>> rhNowHistory(@Param("user_id") Long user_id);
//    @Query("SELECT rh.store.title, rh.rentalAT FROM rental_history rh WHERE rh.user.member_seq = :user_id and rh.checkValue = 0")
//    List rhNowHistory(@Param("user_id") Long user_id);

    // 과거 대여 정보관련 쿼리
//    "SELECT st.title AS rentalStore , rh.rentalAT AS rentalAt FROM rental_history rh Inner JOIN StoreInfo st ON " +
//            "rh.rentalStore.storeId = st.storeId AND rh.user.member_seq = :user_id AND rh.checkValue = 1
    @Query("SELECT st.title AS rentalStore , rh.rentalAT AS rentalAt, re.returnAT AS returnAt, re.returnStore.title AS returnStore, rh.rental_id AS id FROM rental_history rh Inner JOIN StoreInfo st ON " +
            "rh.rentalStore.storeId = st.storeId AND rh.user.member_seq = :user_id AND rh.checkValue = 1 JOIN return_history re ON re.rentalH.rental_id = rh.rental_id")
    List<Map<String, Object>> rhPastHistory(@Param("user_id") Long user_id, Pageable pageable);


    // Query For Return Logic
    @Query("SELECT rh FROM rental_history rh WHERE rh.checkValue = 0 AND rh.cup.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    rental_history RentalHistoryLogic(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);


    // 아직 반납안된 컵의 대여시간을 따지기 위함
//    @Query("SELECT rh FROM rental_history rh INNER JOIN Cup c ON c.cupState = 1 AND c.goodAttitudeCup_Uid = rh.cup.goodAttitudeCup_Uid where rh.user.member_seq = :user_id")
    @Query("SELECT rh FROM rental_history rh WHERE rh.user.member_seq = :user_id and rh.checkValue = 0 and rh.cup.cupState = 1")
    List<rental_history> CheckCupRentalTime(Long user_id);


    /////////////////////// 추후에 삭제되어야 하는 부분 /////////////////////////////
    @Modifying
    @Query("UPDATE rental_history rh SET rh.checkValue = 2 WHERE rh.checkValue = 0")
    void findAllZeroCup();
}
