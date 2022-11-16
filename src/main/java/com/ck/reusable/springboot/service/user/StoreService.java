package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.domain.Store.StoreInfoRepository;
import com.ck.reusable.springboot.web.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreInfoRepository storeInfoRepository;

    @Transactional
    public List<StoreDto.StoreInfoDto> AllStoreInfo()
    {
        return storeInfoRepository.AllStoreInfo().stream()
                .map(StoreDto.StoreInfoDto::new)
                .collect(Collectors.toList());
    }
}
