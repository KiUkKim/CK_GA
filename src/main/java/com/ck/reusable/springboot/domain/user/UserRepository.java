package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 유저 찾기
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);

    // 중복된 전화번호 있는지 체크
    Optional<User> findByTel(String tel);
}
