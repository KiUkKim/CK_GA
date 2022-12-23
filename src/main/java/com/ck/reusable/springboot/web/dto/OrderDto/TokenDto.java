package com.ck.reusable.springboot.web.dto.OrderDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// formJson을 이용하기 위해서 클래스 형식으로 사용
@Data
public class TokenDto {
        private String access_token;
        private long now;
        private long expired_at;
}
