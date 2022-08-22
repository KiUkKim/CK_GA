package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshJwtRepository extends JpaRepository<RefreshJwt, Integer>{

    RefreshJwt findByTokenName(String refreshToken);

    @Query("SELECT J FROM RefreshJwt J WHERE J.user_id = :user_id")
    RefreshJwt findForUser(@Param("user_id") Long user_id);

    @Query("SELECT J.tokenName FROM RefreshJwt J WHERE J.user_id = :user_id")
    String findForUserToken(@Param("user_id") Long user_id);

    @Query("SELECT J.ID FROM RefreshJwt J WHERE J.tokenName = :token_name")
    Long findUserId(@Param("token_name") String token_name);
}
