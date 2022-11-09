package com.ck.reusable.springboot.web.dto;


import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.ck.reusable.springboot.domain.History.return_history;
import com.ck.reusable.springboot.domain.Store.StoreInfo;
import com.ck.reusable.springboot.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReturnHistoryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReturnHistoryResponseDto {
        private LocalDateTime returnAt;
        private rental_history rentalId;
        private StoreInfo storeId;

        @NotNull
        public return_history toEntity() {
            return return_history.builder()
                    .returnAT(returnAt)
                    .returnStore(storeId)
                    .rentalH(rentalId)
                    .build();
        }
    }
}
