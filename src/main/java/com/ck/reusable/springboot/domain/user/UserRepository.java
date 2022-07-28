package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 유저가 db에 저장되어있는지 확인 ( count로 체크 )
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM User c WHERE c.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // 유저 찾기
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
