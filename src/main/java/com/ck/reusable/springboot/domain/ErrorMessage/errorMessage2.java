package com.ck.reusable.springboot.domain.ErrorMessage;

import com.ck.reusable.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class errorMessage2 {

        // 상태코드에 대함
        private String status;

        // 관련 메시지
        private String message;

        @Builder
        public errorMessage2(String status, String message)
        {
            this.status = status;
            this.message = message;
        }
}

