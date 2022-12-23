package com.ck.reusable.springboot.web;

import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage;
import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage2;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.service.Payment.CardInfoService;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.CardInfoDto;
import com.ck.reusable.springboot.web.dto.OrderDto.OrderInfoDto;
import com.ck.reusable.springboot.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class UserCardApiController {

    private final CardInfoService cardInfoService;

    private final UserService userService;

    @PostMapping("/CardSave")
    public Object register(HttpServletRequest request, Principal principal) {
        // 가입과 동시에 객체 정보 넘기기( Service)
        String customer_uid = request.getParameter("merchant_uid");
        String merchant_uid = request.getParameter("customer_uid");

        OrderInfoDto.KaKaoInfo kaKaoInfo = new OrderInfoDto.KaKaoInfo(customer_uid, merchant_uid);

        boolean check = cardInfoService.CardInfoDuplicateCheck(principal.getName());

        if(check == false)
        {
            // 반환을 위해서 사용자 찾아오기
            User user = userService.findUser("123456@naver.com");

            CardInfoDto.ForCardRegisterRequestDto cardRegisterRequestDto = new CardInfoDto.ForCardRegisterRequestDto();

            cardRegisterRequestDto.setUser(user);
            BeanUtils.copyProperties(kaKaoInfo, cardRegisterRequestDto);

            CardInfoDto.ForCardInfoListDto cardInfoList = cardInfoService.registerCard(cardRegisterRequestDto);
//
//            errorMessage errorMessage = com.ck.reusable.springboot.domain.ErrorMessage.errorMessage.builder()
//                    .status("200").message("정상적으로 카드 등록이 완료되었습니다.").user(user).build();

            return new ResponseEntity<>(cardInfoList, HttpStatus.ACCEPTED);
        }

        errorMessage2 er2 = errorMessage2.builder().status("404").message("카드 중복이 되었습니다.").build();

        return  new ResponseEntity<>(er2, HttpStatus.BAD_REQUEST);
    }
}
