package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.History.rental_history;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RentalHistoryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RentalHistoryResponseDto {
        private LocalDateTime rentalAt;
        private User userUid;
        private Cup goodAttitudeCup_Uid;
        private StoreInfo storeId;
        private Integer checkValue;

        @NotNull
        public rental_history toEntity() {
            return rental_history.builder()
                    .rentalAT(rentalAt)
                    .checkValue(checkValue)
                    .cup(goodAttitudeCup_Uid)
                    .store(storeId)
                    .user(userUid)
                    .build();
        }
    }

    // TestLogic
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TestLogicDto{
        LocalDateTime rentalAt;

        String rentalStore;

        public TestLogicDto(rental_history entity, StoreInfo entity2)
        {
            this.rentalAt = entity.getRentalAT();
            this.rentalStore = entity2.getTitle();
        }

    }
}
