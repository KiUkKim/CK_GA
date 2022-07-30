package com.ck.reusable.springboot.domain.ErrorMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class errorMessage3 {

        // 상태코드에 대함
        private String status;

        // 관련 메시지
        private String message;

        // 인증번호 값
        String numStr;

        @Builder
        public errorMessage3(String status, String message, String numStr)
        {
            this.status = status;
            this.message = message;
            this.numStr = numStr;
        }
}
