package com.ck.reusable.springboot.web;


import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.service.Payment.ImportService;
import com.ck.reusable.springboot.service.Payment.PaymentService;
import com.ck.reusable.springboot.web.dto.OrderDto.BillikngKeyDto;
import com.ck.reusable.springboot.web.dto.OrderDto.OrderInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderApiController {
    private final PaymentService paymentService;

    private final ImportService importService;
//    private final PaymentService paymentService;

//    // 카드 결제 성공 후
//    @PostMapping("/api/order/payment/complete")
//    public ResponseEntity<String> paymentComplete(HttpSession session, OrderInfoDto.OrderInfoDto2 orderInfo, long totalPrice, Principal user) throws IOException {
//
//        String token = paymentService.getToken();
//
//        System.out.println("토큰 : " + token);
//        // 결제 완료된 금액
//        int amount = paymentService.paymentInfo(orderInfo.getImpUid(), token);
//
//        try {
//            // 주문 시 사용한 포인트
//            int usedPoint = orderInfo.getUsedPoint();
//
//            if (user != null) {
//                int point = user.getPoint();
//
//                // 사용된 포인트가 유저의 포인트보다 많을 때
//                if (point < usedPoint) {
//                    paymentService.payMentCancle(token, orderInfo.getImpUid(), amount, "유저 포인트 오류");
//                    return new ResponseEntity<String>("유저 포인트 오류", HttpStatus.BAD_REQUEST);
//                }
//
//            } else {
//                // 로그인 하지않았는데 포인트사용 되었을 때
//                if (usedPoint != 0) {
//                    paymentService.payMentCancle(token, orderInfo.getImpUid(), amount, "비회원 포인트사용 오류");
//                    return new ResponseEntity<String>("비회원 포인트 사용 오류", HttpStatus.BAD_REQUEST);
//                }
//            }
//
//            CartListDto cartList = (CartListDto) session.getAttribute("cartList");
//
//            // 실제 계산 금액 가져오기
//            long orderPriceCheck = orderService.orderPriceCheck(cartList) - usedPoint;
//
//            // 계산 된 금액과 실제 금액이 다를 때
//            if (orderPriceCheck != amount) {
//                paymentService.payMentCancle(token, orderInfo.getImpUid(), amount, "결제 금액 오류");
//                return new ResponseEntity<String>("결제 금액 오류, 결제 취소", HttpStatus.BAD_REQUEST);
//            }
//
//            orderService.order(cartList, orderInfo, user, session);
//            session.removeAttribute("cartList");
//
//            return new ResponseEntity<>("주문이 완료되었습니다", HttpStatus.OK);
//
//        } catch (Exception e) {
//            paymentService.payMentCancle(token, orderInfo.getImpUid(), amount, "결제 에러");
//            return new ResponseEntity<String>("결제 에러", HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/subscription/issue-billing")
    public @ResponseBody OrderInfoDto.CardInfoList issue_billing(HttpServletRequest req, HttpServletResponse res) {
        String card_number = req.getParameter("card_number");
        Assert.notNull(card_number, "card number must not be null");

        String expiry = req.getParameter("expiry");
        Assert.notNull(expiry, "expiry number must not be null");

        String birth = req.getParameter("birth");
        Assert.notNull(birth, "birth number must not be null");

        String pwd_2digit = req.getParameter("pwd_2digit");
        Assert.notNull(pwd_2digit, "pwd_2digit number must not be null");

        String customer_uid = req.getParameter("customer_uid");
        Assert.notNull(customer_uid, "customer_uid number must not be null");

        OrderInfoDto.CardInfoList cardInfoList = new OrderInfoDto.CardInfoList(card_number, expiry, birth, pwd_2digit, customer_uid);

        // 빌링키 발급 테스트
        log.info("This is BillingKey : " + importService.RequestTokenInfo(cardInfoList,customer_uid));

        // 빌링키 code부분만 떼내기
        
        return cardInfoList;
    }

    @PostMapping("/subscription/issue-billing2")
    public @ResponseBody OrderInfoDto.KaKaoInfo issue_billing_kakao(HttpServletRequest req, HttpServletResponse res){
        String merchant_uid = req.getParameter("merchant_uid");
        Assert.notNull(merchant_uid, "merchant_uid must not be null");

        String customer_uid = req.getParameter("customer_uid");
        Assert.notNull(customer_uid, "customer_uid must not be null");

        OrderInfoDto.KaKaoInfo kaKaoInfo = new OrderInfoDto.KaKaoInfo(customer_uid, merchant_uid);

        // 빌링키 발급 테스트
        String billingKey = importService.RequestTokenKaKaoInfo(kaKaoInfo, customer_uid);
        Assert.notNull(billingKey, "billing     key is must not be null");

        // 빌링키 발급 정상적으로 됐다면 - db에 정보 저장


        log.info("billingkey : " + billingKey);

        // 빌링키 code부분만 떼내기
        Gson billingKeyInfo = new Gson();

        BillikngKeyDto billikngKeyDto = billingKeyInfo.fromJson(billingKey, BillikngKeyDto.class);

        log.info("billing key code : " + billikngKeyDto.getCode());

//        // 정상적인 통신 완료
//        if(billikngKeyDto.getCode() == "0")
//        {
//            // 카드 정상 승인
//            if(billikngKeyDto.getResponse() == "paid")
//            {
//                //TODO 정상승인시 카드 취소
////                importService.CancelLogic(merchant_uid);
//            }
//
//            // 카드 승인 실패 [한도초과, 잔액부족 등.. ]
//            else{
//                //TODO
//                //승인 실패 띄우기
//            }
//        }

        return null;
    }
//
//    @PostMapping("/test")
//    public void test()
//    {
//        System.out.println(importService.getToken());
//    }
//
//    @GetMapping("/test1")
//    public void test2()
//    {
////        System.out.println(importService.RequestTokenInfo());
//    }
}

