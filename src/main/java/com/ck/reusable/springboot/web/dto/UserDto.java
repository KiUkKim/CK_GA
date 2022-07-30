package com.ck.reusable.springboot.web.dto;

import com.ck.reusable.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 클라이언트에서 전달하는 데이터를 담는 객체


public class UserDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ForUserSignRequestDto{
        private String name;
        private String email;
        private String tel;
        private String passwd;

        @Builder
        public ForUserSignRequestDto(String name, String email, String tel, String passwd){
            this.name = name;
            this.email = email;
            this.tel = tel;
            this.passwd = passwd;
        }

        public User toEntity(){
            return User.builder()
                    .name(name)
                    .email(email)
                    .tel(tel)
                    .password(passwd)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ForUserResponseDto{
        private Long member_seq;
        private String name;
        private String email;
        private String tel;

        public ForUserResponseDto(User user)
        {
            this.member_seq = user.getMember_seq();
            this.name = user.getName();
            this.email = user.getEmail();
            this.tel = user.getTel();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class ForUserLoginRequestDto{
        private String email;
        private String passwd;

        public ForUserLoginRequestDto(User entity)
        {
            this.email = entity.getEmail();
            this.passwd = entity.getPassword();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ForUserLoginResponseDto{
        private String email;
        private String accessToken;

        public ForUserLoginResponseDto(User entity, String tokenProvider)
        {
            this.email = entity.getEmail();
            this.accessToken = tokenProvider;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ForUserListResponseDto{
        private String name;
        private String tel;
        private String email;
        private String roles;
        private String password;

        public ForUserListResponseDto(User user)
        {
            this.name = user.getName();
            this.tel = user.getTel();
            this.email = user.getEmail();
            this.roles = user.getRoles();
            this.password = user.getPassword();
        }

        public User toEntity2(){
            return User.builder()
                    .name(name)
                    .email(email)
                    .tel(tel)
                    .roles(roles)
                    .password(password)
                    .build();
        }
    }

    //////////////////////// 중복 인증 관련 로직 /////////////////////////
    /*
        email 인증 중복 체크 검사
     */
    @Getter
    @NoArgsConstructor
    public static class ForUserValidateDuplicateEmail
    {
        private String email;

        public ForUserValidateDuplicateEmail(User user)
        {
            this.email = user.getEmail();
        }
    }

    /*
        전화번호 인증 중복 체크 검사
     */
    @Getter
    @NoArgsConstructor
    public static class ForUserValidateDuplicateTel
    {
        private String tel;

        public ForUserValidateDuplicateTel(User user)
        {
            this.tel = user.getTel();
        }
    }
}
