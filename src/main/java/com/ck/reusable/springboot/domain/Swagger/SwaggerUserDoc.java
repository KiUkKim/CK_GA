package com.ck.reusable.springboot.domain.Swagger;

import com.ck.reusable.springboot.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class SwaggerUserDoc {


    ////////////////////////// Member Save API ///////////////////////////////////
    @Schema(description = "회원가입")
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerUserSignUpDto {

        @Schema(description = "이름", allowableValues = {"김아무개"}, nullable = false)
        @NotNull
        private String name;

        @Schema(description = "이메일", allowableValues = {"xxxx@xxxx.com"}, nullable = false)
        @NotNull
        private String email;

        @Schema(description = "전화번호", allowableValues = {"010-0000-0000"}, nullable = false)
        @NotNull
        private String tel;

        @Schema(description = "비밀번호", allowableValues = {"xxxxx@w4124"}, nullable = false)
        @NotNull
        private String passwd;

        public SwaggerUserSignUpDto(SwaggerUserDoc.SwaggerUserSignUpDto dto)
        {
            this.name = dto.name;
            this.email = dto.email;
            this.tel = dto.tel;
            this.passwd = dto.passwd;
        }

        // NULL값 체크 (Swagger를 위해서 체크해줘야함)
    }

    @Schema(description = "로그인")
    @Getter
    @NoArgsConstructor
    public static class SwaggerUserSignInDto {


        @Schema(description = "이메일", allowableValues = {"xxxx@xxxx.com"})
        @NotNull
        private String email;

        @Schema(description = "비밀번호", allowableValues = {"xxxxx@w4124"})
        @NotNull
        private String password;

    }

    // 전화번호 인증 관련 DTO
    @Schema(description = "전화번호")
    @Getter
    @NoArgsConstructor
    public static class SwaggerPhoneInputDto{

        @Schema(description = "전화번호", allowableValues = {"01000000000"})
        @NotNull
        private String tel;

    }

    // 전화번호 중복방지 관련 DTO
    @Schema(description = "전화번호")
    @Getter
    @NoArgsConstructor
    public static class SwaggerPhoneInputDto2{

        @Schema(description = "전화번호", allowableValues = {"010-0000-0000"})
        @NotNull
        private String tel;

    }

    // 이메일 관련 DTO
        /*
        email 인증 중복 체크 검사
     */
    @Getter
    @NoArgsConstructor
    public static class ValidateEmailDto {
        private String email;
    }



    ////////////////////////////// 에러 관련 메세지 /////////////////////////////

    // 회원가입 메세지
    // 에러관련 메세지
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerEr
    {
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
        public SwaggerEr(String status, String message, SwaggerUserSignUpDto dto)
        {
            this.status = status;
            this.message = message;
            this.name = dto.getName();
            this.passwd = dto.getPasswd();
            this.tel = dto.getTel();
            this.email = dto.getEmail();
        }
    }

    // 에러관련 메세지
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerEr2
    {
        // 상태코드에 대함
        private String status;

        // 관련 메시지
        private String message;

        @Builder
        public SwaggerEr2(String status, String message)
        {
            this.status = status;
            this.message = message;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerEr3
    {
        // 관련 메시지
        private String cupState;

        @Builder
        public SwaggerEr3(String cupState)
        {
            this.cupState = cupState;
        }
    }


    ///////////////// 토큰 관련 정보 ////////////////////////
    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "로그인 성공시 Header 값으로 반환되는 정보입니다.")
    public static class TokenDto{
        @Schema(description = "AccessToken", allowableValues = {"eyJ0eXAiOiJKV1QiLCJhbGciO-이하생략"})
        private String Authorization;

        @Schema(description = "RefreshToken", allowableValues = {"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUz-이하생략"})
        private String RefreshToken;
    }

    /////////////////// CUP 관련 ////////////////////////////

    // Cup 정보 받기
    @Getter
    @NoArgsConstructor
    @Schema(description = "컵의 QR코드를 찍어 상태를 확인하는 Schema입니다.")
    public static class cupState{

        @Schema(description = "컵 아이디", allowableValues = {"1"})
        @NotNull
        private Long goodAttitudeCup_Uid;
    }


    // CUP 대여 확인
    @Getter
    @NoArgsConstructor
    @Schema(description = "컵 대여를 위한 Schema입니다.")
    public static class cupRentalDto{

        @Schema(description = "컵 아이디", allowableValues = {"1"})
        @NotNull
        private Long goodAttitudeCup_Uid;

        @Schema(description = "회원 번호", allowableValues = {"1"})
        @NotNull
        private Long userUid;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "컵 반납을 위한 Schema입니다.")
    public static class cupReturnDto{

        @Schema(description = "컵 아이디", allowableValues = {"1"})
        @NotNull
        private Long goodAttitudeCup_Uid;

        @Schema(description = "회원 번호", allowableValues = {"1"})
        @NotNull
        private Long userUid;
    }




    //////////////// 매장 관련 //////////////////

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerStoreInfo
    {
        private Long id;
        private double latitude;
        private double longitude;
        private String imageUrl;
        private String title;
        private String business_hours;
        private String tag;

        public SwaggerStoreInfo(Long id, double latitude, double longitude, String imageUrl, String title, String business_hours, String tag)
        {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.imageUrl = imageUrl;
            this.title = title;
            this.business_hours = business_hours;
            this.tag = tag;
        }
    }

}
