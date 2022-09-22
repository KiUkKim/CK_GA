package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.user.Cup;
import com.ck.reusable.springboot.domain.user.StoreInfo;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.rental_history;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalHistoryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RentalHistoryResponseDto {
        private LocalDateTime rentalAt;
        private User userUid;
        private Cup cupUid;
        private StoreInfo storeId;
        private Integer checkValue;

        @NotNull
        public rental_history toEntity() {
            return rental_history.builder()
                    .rentalAT(rentalAt)
                    .checkValue(checkValue)
                    .cup(cupUid)
                    .store(storeId)
                    .user(userUid)
                    .build();
        }
    }
}
