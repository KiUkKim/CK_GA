package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.service.user.StoreService;
import com.ck.reusable.springboot.web.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;

    @GetMapping("/manager/storeInfo")
    public Map<String, Object> PrintStoreAllInfo()
    {
        Map<String, Object> StoreMap = new HashMap<>();

        StoreMap.put("store", storeService.AllStoreInfo());


        return StoreMap;
    }
}
