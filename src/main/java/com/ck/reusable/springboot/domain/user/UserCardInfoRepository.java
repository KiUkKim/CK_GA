package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCardInfoRepository extends JpaRepository<UserCard, Long> {

    // 유저가 db에 저장되어있는지 확인 ( count로 체크 )
    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END FROM UserCard uc WHERE uc.user.email = :email")
    boolean existsByCardInfo(@Param("email") String email);

}
