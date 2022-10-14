package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


}
