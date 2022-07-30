package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage2;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class UserApiController {

    @GetMapping("/home")
    public String test()
    {
        return "<h1>home<h1>";
    }

    @PostMapping("/token")
    public String test2(){return "<h1>token<h1>";}

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

//     회원가입
    @PostMapping("/save")
    public Object register(@RequestBody UserDto.ForUserSignRequestDto forUserDto) {
        // 가입과 동시에 객체 정보 넘기기( Service)

        boolean check = userService.validateDuplicated(forUserDto.getEmail());


        if(check == false)
        {
            UserDto.ForUserResponseDto forUserResponseDto = userService.registerMember(forUserDto);

            // 반환을 위해서 사용자 찾아오기
            User user = userService.findUser(forUserResponseDto.getEmail());

            errorMessage errorMessage = com.ck.reusable.springboot.domain.ErrorMessage.errorMessage.builder()
                    .status("200").message("정상적으로 회원가입이 완료되었습니다.").user(user).build();

            return new ResponseEntity<>(errorMessage, HttpStatus.ACCEPTED);
        }

        errorMessage2 er2 = errorMessage2.builder().status("404").message("회원가입이 중복되었습니다.").build();

        return  new ResponseEntity<>(er2, HttpStatus.BAD_REQUEST);
    }

    // 회원가입2
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료";
    }

    // 권한 테스트
    @PostMapping("/api/v1/user")
    public String test1()
    {
        return "user";
    }

}
