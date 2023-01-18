package com.ck.reusable.springboot.domain.Cup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CupRepository extends JpaRepository<Cup, Long> {

    /*
    Cup State Check Repository
     */
//    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cup c WHERE c.cupUid = :cupUid")
//    boolean CanRental(@Param("cupUid") Long cupUid);

    @Query("SELECT c.cupState From Cup c WHERE c.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    Integer CanRental(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

    /*
    Cup 대여시 값 변경
     */
    @Modifying
    @Query("update Cup c set c.cupState = 1 WHERE c.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    void UpdateCupState(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

    @Query("SELECT c From Cup c WHERE c.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    Cup cupReturn(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);


    //////////////// 나중에 삭제되어야 할 부분 ////////////////////////////
    //////////////// 초기화를 위해서 넣어둠 ///////////////////////////////
    @Query("SELECT c FROM Cup c")
    List<Cup> findAllCup();

    // 컵 분실 신고시 값 변경
    @Modifying
    @Query("update Cup c set c.cupState = 4 WHERE c.goodAttitudeCup_Uid = :goodAttitudeCup_Uid")
    void UpdateCupLostState(@Param("goodAttitudeCup_Uid") Long goodAttitudeCup_Uid);

}
