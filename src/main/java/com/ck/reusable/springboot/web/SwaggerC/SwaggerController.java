package com.ck.reusable.springboot.web.SwaggerC;

import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage;
import com.ck.reusable.springboot.domain.Swagger.Swagger;
import com.ck.reusable.springboot.domain.Swagger.SwaggerUserDoc;
import com.ck.reusable.springboot.domain.Swagger.SwaggerUserOutPutDoc;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.service.user.RentalHistoryService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SwaggerController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private final RentalHistoryService rentalHistoryService;

    // 회원가입
    @Operation(summary = "Sign Up API", description = "회원가입에 필요한 API 입니다.", tags = {"User Save API"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.SwaggerSaveResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/save")
    public Object swaggerUserSignDto(@RequestBody SwaggerUserDoc.SwaggerUserSignUpDto swaggerdto)
    {
        swaggerdto.setPasswd(bCryptPasswordEncoder.encode(swaggerdto.getPasswd()));

        SwaggerUserDoc.SwaggerEr er = SwaggerUserDoc.SwaggerEr.builder().status("201").message("정상적으로 회원가입이 완료되었습니다.").dto(swaggerdto).build();
        return new ResponseEntity<>(er, HttpStatus.CREATED);
    }

    // 로그인
    @Operation(summary = "Sign In API", description = "로그인에 필요한 API 입니다.", tags = {"User Save API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/jwt",
                    schema = @Schema(implementation = SwaggerUserDoc.TokenDto.class)) ),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br/>정보가 불일치합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/login")
    public Object swaggerUserSignInDto(@RequestBody SwaggerUserDoc.SwaggerUserSignInDto swaggerdto)
    {
        String message = "정상적으로 로그인 되었습니다.";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2tlblRlc3QiLCJpc3MiOiJodHRwczovL2NrLXJldXNhYmxlYXBwLmhlcm9rdWFwcC5jb20vIiwiaWQiOjMsImV4cCI6MTY3MjM4MjE2NSwiZW1haWwiOiIxMjM0NUBuYXZlci5jb20ifQ.EhiOvEXnfypAfgoK2U7syNOcEPhH8z1421421412");
        headers.set("RefreshToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2tlblRlc3QiLCJpc3MiOiJodHRwczovL2NrLXJldXNhYmxlYXBwLmhlcm9rdWFwcC5jb20vIiwiaWQiOjMsImV4cCI6MTY3MjM4MjE2NSwiZW1haWwiOiIxMjM0NUBuYXZlci5jb20ifQ.EhiOvEXnfypAfgoK2U7syNOcEPhH8z1421421412");

        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    // 핸드폰 번호 인증 발송
    @Operation(summary = "Send For Validation Of Phone Number", description = "회원가입시 필요한 핸드폰 인증번호 API 입니다.", tags = {"User Save API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증번호 반환", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.PhoneValiNum.class))),
            @ApiResponse(responseCode = "409", description = "Conflict <br/>중복되는 전화번호입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.PhoneValiError.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/phoneCheck")
    public Object swaggerPhoneDto(@RequestBody SwaggerUserDoc.SwaggerPhoneInputDto swaggerdto)
    {
        String numStr = "3421";

        return new ResponseEntity<>(numStr, HttpStatus.OK);
    }

    // 이메일 중복 확인
    @Operation(summary = "Validation of Email", description = "중복된 이메일 회원 가입 방지를 위한 API", tags = {"User Save API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.EmailValiOk.class))),
            @ApiResponse(responseCode = "409", description = "Conflict <br/>중복되는 이메일입니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.EmailValiErr.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/api/emailCheck")
    public Object swaggerEmailDto(@RequestBody SwaggerUserDoc.ValidateEmailDto swaggerdto)
    {
        SwaggerUserDoc.SwaggerEr2 er = SwaggerUserDoc.SwaggerEr2.builder().status("200").message("중복된 이메일이 존재하지 않으므로 사용가능합니다.").build();
        return new ResponseEntity<>(er, HttpStatus.OK);
    }

    //로그인 회원 정보 확인
    @Operation(summary = "User Info", description = "유저정보를 확인하는 API", tags = {"User API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.userInfoDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br>비인가된 사용자", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/api/user/userInfo")
    public Object swaggerUserDto()
    {
        /*
        유저 정보 출력하는 구간 - db name != dto name -> 출력이 안되므로 seq만 다르게 뽑아서 출력
         */
        UserDto.ForUserTokenResponseDto forUserTokenResponseDto = new UserDto.ForUserTokenResponseDto();
        User user = userService.searchUserByEmail2("123456@naver.com");

        BeanUtils.copyProperties(user, forUserTokenResponseDto);

        forUserTokenResponseDto.setUId(user.getMember_seq());
        /*

         */
        Long user_id = userService.userIdByEmail("123456@naver.com");

        //TODO
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> nowRental = rentalHistoryService.InfoNowRentalHistory(user_id);
        /*
        담겨온 정보 list에 넣어줌
         */
        forUserTokenResponseDto.setRentalStatus(nowRental);

        return new ResponseEntity<>(forUserTokenResponseDto, HttpStatus.OK);
    }


    // 컵 상태 체크 확인
    @Operation(summary = "CupState Info", description = "컵 상태를 확인하는 API", tags = {"Manager API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK<br>대여중인 컵의 경우 자동반납 처리 됩니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.CupStateOutput.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br>비인가된 사용자", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/api/manager/CupStateCheck")
    public Object CupApi(@RequestBody SwaggerUserDoc.cupState dto)
    {
        SwaggerUserDoc.SwaggerEr3 er = SwaggerUserDoc.SwaggerEr3.builder().cupState("available").build();
        return new ResponseEntity<>(er, HttpStatus.OK);
    }


    // 컵 대여
    @Operation(summary = "Cup Rental", description = "컵 대여 API", tags = {"Manager API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK<br>컵 대여 결과가 표시됩니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.CupStateOutput.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br>비인가된 사용자", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/api/manager/CupRental")
    public Object CupRental(@RequestBody SwaggerUserDoc.cupRentalDto dto)
    {
        SwaggerUserDoc.SwaggerEr3 er = SwaggerUserDoc.SwaggerEr3.builder().cupState("using").build();
        return new ResponseEntity<>(er, HttpStatus.OK);
    }


    // 매장정보
    @Operation(summary = "Store Info", description = "매장 정보 API", tags = {"User API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK<br>해당 형식에서는 한 개의 매장만 보여줍니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.StoreOutPut.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br>비인가된 사용자", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/api/user/storeInfo")
    public Object storeInfo()
    {
        SwaggerUserDoc.SwaggerStoreInfo swaggerStoreInfo = new SwaggerUserDoc.SwaggerStoreInfo(1L, 36.633717781404734, 127.45759570354707, "url.com", "왕큰손파닭", "17:00~20:00", "중문,많음");
        return new ResponseEntity<>(swaggerStoreInfo, HttpStatus.OK);
    }

    // 대여기록 pagination
    @Operation(summary = "Rental History Info", description = "과거 대여 기록 정보 API<br>Parameter의 sort \"string\"값을 \"id\"로 변경해야 정상작동합니다.  ", tags = {"User API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK<br>해당 형식에서는 일부 목록만 보여줍니다.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerUserOutPutDoc.userHistroyInfoDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized <br>비인가된 사용자", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/api/user/userHistoryInfo")
    public Object HistoryInfo(@RequestParam(name = "size") Integer size, @RequestParam(name = "page") Integer page, Pageable pageable)
    {
        /*
        유저 정보 출력하는 구간 - db name != dto name -> 출력이 안되므로 seq만 다르게 뽑아서 출력
         */
        UserDto.ForUserHistoryResponseDto historyResponseDto = new UserDto.ForUserHistoryResponseDto();

        User user = userService.searchUserByEmail2("123456@naver.com");

        System.out.println(user);

        BeanUtils.copyProperties(user, historyResponseDto);

        historyResponseDto.setUId(user.getMember_seq());

        /*

         */
        Long user_id = userService.userIdByEmail("123456@naver.com");

        //TODO
        // rental_history 부분 ,, 현재 대여 기록 뽑아오는 것과, 과거(반납된 부분) 기록 뽑아 오는 것 고민하기!

        // 여러 개 정보들을 한곳에 합쳐주기 위함
        List<Map<String, Object>> pastRental = rentalHistoryService.InfoPastRentalHistory(user_id, pageable);

        /*
        담겨온 정보 list에 넣어줌
         */
        historyResponseDto.setHistory(pastRental);

        return new ResponseEntity<>(historyResponseDto, HttpStatus.OK);
    }

}
