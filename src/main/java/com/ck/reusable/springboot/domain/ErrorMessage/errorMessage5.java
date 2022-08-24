package com.ck.reusable.springboot.domain.ErrorMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class errorMessage5 {

        // 상태코드에 대함
        private String status;

        // 관련 메시지
        private String message;

        // 리프레쉬 토큰 값
        private String refreshToken;


        @Builder
        public errorMessage5(String status, String message, String refreshToken)
        {
            this.status = status;
            this.message = message;
            this.refreshToken = refreshToken;
        }
}
