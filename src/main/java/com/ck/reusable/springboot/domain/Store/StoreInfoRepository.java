package com.ck.reusable.springboot.domain.Store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {

    @Query("SELECT s FROM StoreInfo s WHERE s.storeId = :storeId")
    StoreInfo ReturnStoreId(@Param("storeId") Long storeId);

    @Query("SELECT s FROM StoreInfo  s")
    List<StoreInfo> AllStoreInfo();

}
