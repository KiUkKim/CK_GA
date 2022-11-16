package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.Store.StoreInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class StoreInfoDto
    {
        private Long id;
        private double latitude;
        private double longtitude;
        private String imageUrl;
        private String title;
        private String business_hours;
        private String tag;

        public StoreInfoDto(StoreInfo storeInfo)
        {
            this.id = storeInfo.getStoreId();
            this.latitude = storeInfo.getLatitude();
            this.longtitude = storeInfo.getLongitude();
            this.imageUrl = storeInfo.getImage_url();
            this.title = storeInfo.getTitle();
            this.business_hours = storeInfo.getBusiness_hours();
            this.tag = storeInfo.getTag();
        }
    }

}
