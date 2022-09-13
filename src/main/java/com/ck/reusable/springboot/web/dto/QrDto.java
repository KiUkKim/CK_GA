package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.user.User;
import lombok.Getter;

public class QrDto {


    @Getter
    public static class ForQrResponseDto{
            Long cup_uid;
            Long user_uid;
    }
}
