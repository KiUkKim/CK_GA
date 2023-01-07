package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.Cup.Cup;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage2;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage3;
import com.ck.reusable.springboot.domain.History.rental_history;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.service.user.CupService;
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
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    private final UserVertificationService vertificationService;

    private final RentalHistoryService rentalHistoryService;

    private final CupService cupService; // 나중에 삭제시켜주어야함

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

        Long user_id = userService.userIdByEmail(principal.getName());
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 현재 벤 유저가 아니면 대여한 컵을 기준으로 따져주기
        if(user.getBanUser() == 0)
        {
            // TODO
            // 대여기록 비교해서 벤 유저 인지 파악하기

            List<rental_history> date = rentalHistoryService.CheckDateService(user_id);

            for(int i = 0; i < date.size(); i++)
            {
                LocalDateTime today = LocalDateTime.now();

                LocalDateTime rentalDate = date.get(i).getRentalAT();

                rentalDate = rentalDate.plusMinutes(10);

                if(rentalDate.isBefore(today))
                {
                    user.setBanUser(1);
                    forUserTokenResponseDto.setBanUser(1);
                    break;
                }
            }
        }

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> nowRental = rentalHistoryService.InfoNowRentalHistory(user_id);

        /*
        담겨온 정보 list에 넣어줌
         */
        forUserTokenResponseDto.setRentalStatus(nowRental);

        return forUserTokenResponseDto;
    }

    @GetMapping("/user/userHistoryInfo")
    public List<Map<String, Object>> searchLoginUserHistory(Principal principal, Pageable pageable, @RequestParam(name = "size") Integer size, @RequestParam(name = "page") Integer page)
    {
        Long user_id = userService.userIdByEmail(principal.getName());

        //TODO
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> pastRental = rentalHistoryService.InfoPastRentalHistory(user_id, pageable);

        /*
        담겨온 정보 list에 넣어줌
         */
        return pastRental;
    }

    ///////////////// Clear API ///////////////////////
    /////////////// 나중에 삭제해주기 ////////////////

    // TODO
    // 현재 모든 유저 now_cnt 초기화, 모든 컵 상태 - 0, rental 부분 0 -> 1로 변환
    @GetMapping("/userClear")
    public Object clearUser()
    {
        List<User> user = userService.findUserAll();


        // 유저 초기화
        // now_cnt초기화 - 계정 락 풀기
        for(int i = 0; i < user.size(); i++)
        {
            user.get(i).setNow_cnt(0);
            user.get(i).setBanUser(0);
        }

        // 컵 상태 초기화
        List<Cup> cup = cupService.cupInitial();

        for(int i = 0; i < cup.size(); i++)
        {
            cup.get(i).setCupState(0);
        }


        // rental 영역 초기화
        // 0인 부분 찾아서 - 1 반납된 상태로 돌리기
        rentalHistoryService.checkZeroCup();

        return new ResponseEntity<>("유저, 컵, 대여 부분이 초기화 되었습니다.", HttpStatus.OK);
    }

}
