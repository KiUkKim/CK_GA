package com.ck.reusable.springboot.domain.ErrorMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class errorMessage7 {

        // 상태코드에 대함
        private String status;

        // 관련 메시지
        private String message;

        // 리프레쉬 토큰 값
        private String refreshToken;

        // url
        private String url;


        @Builder
        public errorMessage7(String status, String message, String refreshToken, String url)
        {
            this.status = status;
            this.message = message;
            this.refreshToken = refreshToken;
            this.url = url;
        }
}
