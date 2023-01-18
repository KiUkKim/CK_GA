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
            Long goodAttitudeCup_Uid;
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
        Long goodAttitudeCup_Uid;
    }

    @Getter
    @Setter
    public static class ForCupLostResponseDto
    {
        Long goodAttitudeCup_Uid;
    }


}
