package com.ck.reusable.springboot.web.dto.OrderDto;


import lombok.Getter;
import lombok.Setter;

import javax.smartcardio.Card;

public class OrderInfoDto {

    @Getter
    @Setter
    public static class OrderInfoDto2{
        private String impUid; // 아임포트 결제번호
    }

    @Getter
    @Setter
    public static class CardInfoList
    {
        private String card_number;
        private String expiry;
        private String birth;
        private String pwd_2digit;
        private String customer_uid;

        public CardInfoList(String card_number, String expiry, String birth, String pwd_2digit, String customer_uid)
        {
            this.setCard_number(card_number);
            this.setExpiry(expiry);
            this.setBirth(birth);
            this.setPwd_2digit(pwd_2digit);
            this.setCustomer_uid(customer_uid);
        }
    }

    @Getter
    @Setter
    public static class KaKaoInfo{
        private String customer_uid;
        private String merchant_uid;
        private Long amount = 0L;
        private String name = "카드 등록";

        public KaKaoInfo(String customer_uid, String merchant_uid)
        {
            this.setCustomer_uid(customer_uid);
            this.setMerchant_uid(merchant_uid);
        }
    }

}
