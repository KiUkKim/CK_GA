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



//    private final PasswordEncoder passwordEncoder;

//    private final AuthenticationManager authenticationManager;
//
//    @PostMapping("/user/save")
//    @ResponseBody
//    public Object userInformation(@RequestBody UserDto.ForUserDto forUserDto)
//    {
//        // 사용자가 db에 존재하는 경우 이미 존재하는 사용자 임을 알리기 위함.
//        boolean check = userService.checkUserDuplicate(forUserDto.getEmail());
//
//
//        // 사용자가 db에 존재하는 경우, run status code or comment;
//        // 회원가입하려는 사용자가 이미 db에 저장되어 있을 경우, Status Code 409 [conflict 발송 ]
//        if(check)
//        {
//            User user = userService.findUserService(forUserDto.getEmail());
//
//            errorMessage errormessage = com.ck.reusable.springboot.domain.ErrorMessage.errorMessage.builder()
//                    .status("409").user(user).build();
//
//            return new ResponseEntity<>(errormessage, HttpStatus.CONFLICT);
//
//        }
//
//
//        // 회원가입하려는 사용자가 db에 존재하지 않는 경우, db에 회원 가입하려는 사용자를 저장하고 Status Code 200 전송
//        else{
//            // MemberRole역할 생성
////            Role role = new Role();
//
////            // Spring Bean copy utility(annotation) for bean objectives
////            UserDto.ForUserDto forUserDto1 = new UserDto.ForUserDto();
//
//
//            // Spring Security for User(Member Password)
////            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////            forUserDto1.setPasswd(passwordEncoder.encode(forUserDto.getPasswd()));
////            forUserDto.setPasswd(passwordEncoder.encode(forUserDto.getPasswd()));
////            BeanUtils.copyProperties(forUserDto, forUserDto1);
////            role.
//            // 계정 정보 저장
//            userService.save(forUserDto);
//
//            // 해당 계정 검색 (db 저장 후, 판단)
//            User user = userService.findUserService(forUserDto.getEmail());
//
//            errorMessage errormessage = com.ck.reusable.springboot.domain.ErrorMessage.errorMessage.builder().
//                    status("200").user(user).build();
//
//            return new ResponseEntity<>(errormessage, HttpStatus.OK);
//        }
//    }
//
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


//     로그인
//    @PostMapping("/member/login")
//    public String login(@RequestBody UserDto.ForUserLoginRequestDto userDto) {
//        String email = userDto.getEmail();
//
//        User user = userService.findUser(email);
//
//        Assert.assertNotNull(user, "등록되지 않은 계정입니다. 이메일을 확인해주세요.");
//
//        String password = user.getPassword();
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//
//        // token 객체로 변경
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
//
//        // Authentication Manager 이용
//        AuthenticationManager authentication = authenticationManager.authenticate(token);
//
//
//
//
//    }

}
