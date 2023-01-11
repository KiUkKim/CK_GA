package com.ck.reusable.springboot.domain.Swagger;

import com.ck.reusable.springboot.domain.Store.StoreInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwaggerUserOutPutDoc {


    ////////////////////////// Member Save API ///////////////////////////////////

    // 회원가입시 반환 메세지
    // 에러관련 메세지
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SwaggerSaveResponseDto
    {
        // 상태코드에 대함
        @Schema(description = "상태", allowableValues = {"200"})
        private String status;

        // 관련 메시지
        @Schema(description = "메시지", allowableValues = {"정상적으로 회원가입이 완료되었습니다."})
        private String message;

        // 유저 정보에 대함
        @Schema(description = "이름", allowableValues = {"김아무개"}, nullable = false)
        private String name;

        @Schema(description = "이메일", allowableValues = {"xxxx@xxxx.com"}, nullable = false)
        private String email;

        @Schema(description = "전화번호", allowableValues = {"010-0000-0000"}, nullable = false)
        private String tel;

        @Schema(description = "비밀번호", allowableValues = {"$2a$10$Y0hfUw9n1L7WmJBg1Ae6y"}, nullable = false)
        private String passwd;

    }

    // 전화번호 인증 실패
    @Getter
    @NoArgsConstructor
    public static class PhoneValiError{
        @Schema(description = "메세지", allowableValues = {"중복된 전화번호가 존재합니다."})
        private String message;

        @Schema(description = "상태코드", allowableValues = {"409"})
        private String status;

        @Schema(description = "인증번호", allowableValues = {"중복된 전화번호"})
        private String numStr;
    }

    // 전화번호 인증 번호 반환
    @Getter
    @NoArgsConstructor
    public static class PhoneValiNum{
        @Schema(description = "인증번호", allowableValues = {"1421"})
        private String numStr;

        @Schema(description = "메세지", allowableValues = {"회원가입 가능한 전화번호"})
        private String message;

        @Schema(description = "상태코드", allowableValues = {"200"})
        private String status;
    }


    // 이메일 인증 중복 에러
    @Getter
    @NoArgsConstructor
    public static class EmailValiErr{
        @Schema(description = "메세지", allowableValues = {"중복된 이메일이 존재합니다."})
        private String message;

        @Schema(description = "상태코드", allowableValues = {"409"})
        private String status;
    }

    // 이메일 인증 반환
    @Getter
    @NoArgsConstructor
    public static class EmailValiOk{
        @Schema(description = "메세지", allowableValues = {"중복된 이메일이 존재하지 않으므로 사용가능합니다."})
        private String message;

        @Schema(description = "상태코드", allowableValues = {"200"})
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class userInfoDto{
        /*
        회원 정보
         */

        @Schema(description = "유저 식별 번호", allowableValues = {"1"})
        private Long uId;

        @Schema(description = "유저 이름", allowableValues = {"김아무개"})
        private String name;

        @Schema(description = "유저 번호", allowableValues = {"010-0000-0000"})
        private String tel;

        @Schema(description = "유저 이메일", allowableValues = {"xxxx@xxxx.com"})
        private String email;

        @Schema(description = "현재 대여중인 컵 개수", allowableValues = {"1"})
        private Integer now_cnt;

        @Schema(description = "총 빌린 컵 개수", allowableValues = {"1"})
        private Integer total_cnt;

        @Schema(description = "해당 유정 락 여부 [false - 사용 가능 계정, true - 사용 불가능 계정( 일정시간 지난 미반납 컵 보유 중)", allowableValues = {"false"})
        private Boolean banUser;

        // 대여기록, 과거 대여 기록 관련 부분.
        @Schema(description = "현재 대여 기록", allowableValues = {"1"})
        List<Map<String, Object>> rentalStatus = new ArrayList();
    }


    @Getter
    @NoArgsConstructor
    public static class CupStateOutput{
        @Schema(description = "컵 상태 반환<br>대여중인 컵을 찍으면 자동 반환됩니다.", allowableValues = {"avaliable", "using", "returned", "cleanse", "loss"})
        private String cupState;
    }


    ///////////////// 매장 정보 //////////////
    @Getter
    @NoArgsConstructor
    public static class StoreOutPut{
        @Schema(description = "매장 ID", allowableValues = "1")
        private Long id;

        @Schema(description = "매장 위도", allowableValues = "36.633717781404734")
        private double latitude;

        @Schema(description = "매장 경도", allowableValues = "127.45759570354707")
        private double longtitude;

        @Schema(description = "이미지 Url", allowableValues = "url.com")
        private String imageUrl;

        @Schema(description = "매장 이름", allowableValues = "왕큰손파닭")
        private String title;

        @Schema(description = "매장 영업 시간", allowableValues = "17:00~20:00")
        private String business_hours;

        @Schema(description = "매장 태그", allowableValues = "중문,많음")
        private String tag;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class userHistroyInfoDto{
        /*
        회원 정보
         */
        @Schema(description = "과거 대여 기록", allowableValues = {"1"})
        List<Map<String, Object>> history = new ArrayList();

    }




}
