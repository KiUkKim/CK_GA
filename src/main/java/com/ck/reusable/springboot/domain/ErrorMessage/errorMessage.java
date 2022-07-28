package com.ck.reusable.springboot.domain.ErrorMessage;

import com.ck.reusable.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class errorMessage {

    // 상태코드에 대함
    private String status;

    // 관련 메시지
    private String message;

    // 유저 정보에 대함
    private String name;
    private String passwd;
    private String tel;
    private String email;

    @Builder
    public errorMessage(String status, String message, User user)
    {
        this.status = status;
        this.message = message;
        this.name = user.getName();
        this.passwd = user.getPassword();
        this.tel = user.getTel();
        this.email = user.getEmail();
    }
}
