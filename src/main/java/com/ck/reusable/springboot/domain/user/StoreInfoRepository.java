package com.ck.reusable.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {

    @Query("SELECT s FROM StoreInfo s WHERE s.storeId = :storeId")
    StoreInfo ReturnStoreId(@Param("storeId") Long storeId);

}
