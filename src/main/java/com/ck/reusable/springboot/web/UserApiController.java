package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage2;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage3;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.service.user.RentalHistoryService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.service.user.UserVertificationService;
import com.ck.reusable.springboot.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    private final UserVertificationService vertificationService;

    private final RentalHistoryService rentalHistoryService;

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

    // 메시지 보내는 api
    @PostMapping("/phoneCheck")
    public @ResponseBody Object memberPhoneCheck(@RequestBody UserDto.ForUserValidateDuplicateTel duplicateTel){

        if(userService.validateDuplicatedTel(duplicateTel.getTel()))
        {
            errorMessage3 er3 = errorMessage3.builder().message("중복된 전화번호가 존재합니다.").status("409").numStr("중복된 전화번호").build();

            return new ResponseEntity<>(er3, HttpStatus.CONFLICT);
        }

        String to = duplicateTel.getTel();

        String numStr =  vertificationService.ckReusableAppNumCheck(to);

        errorMessage3 er3 = errorMessage3.builder().message("회원가입 가능한 전화번호").status("200").numStr(numStr).build();

        return new ResponseEntity<>(er3, HttpStatus.ACCEPTED);
    }

    //TODO
    // 핸드폰 번호 인증 관련 로직 ( 생각해봐야 함 )

    // 이메일 중복 체크 (이미 인증된 전화번호 404 반환)
    @PostMapping("/emailValidate")
    @ResponseBody
    public Object memberEmailCheck(@RequestBody UserDto.ForUserValidateDuplicateEmail duplicateEmail)
    {
        if(userService.validateDuplicated(duplicateEmail.getEmail()))
        {
            errorMessage2 er2 = errorMessage2.builder().message("중복된 이메일이 존재합니다.").status("409").build();

            return new ResponseEntity<>(er2, HttpStatus.CONFLICT);
        }

        errorMessage2 er2 = errorMessage2.builder().message("중복된 이메일이 존재하지 않으므로 사용가능합니다.").status("200").build();

        return new ResponseEntity<>(er2, HttpStatus.ACCEPTED);
    }


    /*
     TEST Logic
     */

    /*
    User 정보 출력
     */
    @GetMapping("/user/userInfo")
    public UserDto.ForUserTokenResponseDto searchLoginUser(Principal principal) {
        /*
        유저 정보 출력하는 구간 - db name != dto name -> 출력이 안되므로 seq만 다르게 뽑아서 출력
         */
        UserDto.ForUserTokenResponseDto forUserTokenResponseDto = new UserDto.ForUserTokenResponseDto();
        User user = userService.searchUserByEmail2(principal.getName());

        System.out.println(user);

        BeanUtils.copyProperties(user, forUserTokenResponseDto);

        forUserTokenResponseDto.setUId(user.getMember_seq());

        /*

         */
        Long user_id = userService.userIdByEmail(principal.getName());

        //TODO
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> nowRental = rentalHistoryService.InfoNowRentalHistory(user_id);

//        List<Map<String, Object>> pastRental = rentalHistoryService.InfoPastRentalHistory(user_id);

        /*
        담겨온 정보 list에 넣어줌
         */
        forUserTokenResponseDto.setRentalStatus(nowRental);

//        forUserTokenResponseDto.setHistory(pastRental);

        return forUserTokenResponseDto;
    }

    @GetMapping("/user/userHistoryInfo")
    public UserDto.ForUserHistoryResponseDto searchLoginUserHistory(Principal principal, Pageable pageable, @RequestParam(name = "size") Integer size, @RequestParam(name = "page") Integer page)
    {
        /*
        유저 정보 출력하는 구간 - db name != dto name -> 출력이 안되므로 seq만 다르게 뽑아서 출력
         */
        UserDto.ForUserHistoryResponseDto historyResponseDto = new UserDto.ForUserHistoryResponseDto();

        /*

         */
        Long user_id = userService.userIdByEmail(principal.getName());

        //TODO
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> pastRental = rentalHistoryService.InfoPastRentalHistory(user_id, pageable);

        /*
        담겨온 정보 list에 넣어줌
         */
        historyResponseDto.setHistory(pastRental);

        return historyResponseDto;
    }


}
