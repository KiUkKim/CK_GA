package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserCard;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CardInfoDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ForCardRegisterRequestDto {
        private String merchant_uid;
        private String customer_uid;
        private User user;

        @Builder
        public ForCardRegisterRequestDto(String merchant_uid, String customer_uid, User user) {
            this.customer_uid = customer_uid;
            this.merchant_uid = merchant_uid;
            this.user = user;
        }

        public UserCard toEntity() {
            return UserCard.builder()
                    .customer_uid(customer_uid)
                    .merchant_uid(merchant_uid)
                    .user(user)
                    .build();
        }
    }

    @Getter
    public static class ForCardInfoListDto{
        private String merchant_uid;
        private String customer_uid;
        private String name;

        public ForCardInfoListDto(String merchant_uid, String customer_uid, UserCard usercard)
        {
            this.customer_uid = customer_uid;
            this.merchant_uid = merchant_uid;
            this.name = usercard.getUser().getName();
        }
    }
}
