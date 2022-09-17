package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CupRepository extends JpaRepository<Cup, Long> {

    /*
    Cup State Check Repository
     */
//    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cup c WHERE c.cupUid = :cupUid")
//    boolean CanRental(@Param("cupUid") Long cupUid);

    @Query("SELECT c.cupState From Cup c WHERE c.cupUid = :cupUid")
    Integer CanRental(@Param("cupUid") Long cupUid);

}
