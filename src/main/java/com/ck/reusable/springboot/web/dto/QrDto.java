package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class QrDto {


    /*
    Cup UID Dto
     */
    @Getter
    public static class ForQrResponseDto{
            Long cup_uid;
    }

    /*
    Cup State Return Message
     */
    @Getter
    @Setter
    public static class ForCupStateResponseDto
    {
        String cupState;
    }

    /*
    Cup Renteal Return Message
     */
    @Getter
    @Setter
    public static class ForCupRentalResponseDto
    {
        Long userUid;
        Long cupUid;
    }
}
