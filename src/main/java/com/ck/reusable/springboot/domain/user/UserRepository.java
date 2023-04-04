package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT u.member_seq FROM User u WHERE u.email = :email")
    Long findUserIdByEmail(@Param("email") String email);

    // 유저 정보 출력
    @Query("SELECT u FROM User u WHERE u.email = :email")
    List<User> findUserInfoByEmail(String email);

    //TODO
    // 테스트 완료 한 후에, 이름 명확하게 바꿀 것!
    // 유저 정보 출력
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserInfoByEmail2(@Param("email") String email);

    Optional<User> findByEmail(String email);

    // 중복된 전화번호 있는지 체크
    Optional<User> findByTel(String tel);

    @Query("SELECT u.email FROM User u WHERE u.member_seq = :user_id")
    String findEmailByUser_id(@Param("user_id") Long user_id);

    @Query("SELECT u.member_seq FROM User u WHERE u.member_seq = :user_id")
    String findMemSeqByUser_id(@Param("user_id") Long user_id);

    /*
    User 최대 컵 개수 체크
     */
    @Query("SELECT u.now_cnt FROM User u WHERE u.email = :email")
    Integer UserNowCnt(@Param("email") String email);

    // 컵 대여 부분
    // 생각해야 하는 부문 : user의 now_cnt 증가, total_cnt 증가 - 해당 유저와 rental history 연결
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.now_cnt = u.now_cnt + 1, u.total_cnt = u.total_cnt + 1 WHERE u.email = :email")
    Integer UpdateUserCnt(@Param("email") String email);

    // 컵 대여 부분
    // 출력을 위한 User 이름 반환
    @Query("SELECT u.name FROM User u WHERE u.email = :email")
    String PrintName(@Param("email") String email);

    @Query("SELECT u.name FROM User u WHERE u.member_seq = :userUid")
    String PrintUserName(@Param("userUid") Long userUid);

    /*
    Cup 반납시 사용하는 부분
     */
    @Modifying
    @Query("update User u set u.now_cnt = u.now_cnt - 1 WHERE u.email = :email")
    void updateReturnUserCnt(@Param("email") String email);

    /*
    매장 정보 반환
     */
    @Query("SELECT u.storeInfo.storeId FROM User u WHERE u.email = :email")
    Long returnStoreInfo(@Param("email") String email);

    //////////////////////// 나중에 삭제되어야 하는 부분 ////////////////////
    // 유저 찾기
    @Query("SELECT u FROM User u")
    List<User> findAllUser();

}
