package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 유저가 db에 저장되어있는지 확인 ( count로 체크 )
//    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM User c WHERE c.email = :email")
//    boolean existsByEmail(@Param("email") String email);

    // 유저 찾기
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);

    // 유저 정보 출력
    @Query("SELECT u FROM User u WHERE u.email = :email")
    List<User> findUserInfoByEmail(String email);

    Optional<User> findByEmail(String email);

    // 중복된 전화번호 있는지 체크
    Optional<User> findByTel(String tel);

    @Query("SELECT u.email FROM User u WHERE u.member_seq = :user_id")
    String findEmailByUser_id(@Param("user_id") Long user_id);

    @Query("SELECT u.member_seq FROM User u WHERE u.member_seq = :user_id")
    String findMemSeqByUser_id(@Param("user_id") Long user_id);
}
