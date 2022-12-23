package com.ck.reusable.springboot.service.Payment;

import com.ck.reusable.springboot.PrivateInterFace.PrivatePaymentIF;
import com.ck.reusable.springboot.web.dto.OrderDto.BillikngKeyDto;
import com.ck.reusable.springboot.web.dto.OrderDto.OrderInfoDto;
import com.ck.reusable.springboot.web.dto.OrderDto.TokenDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ImportService {

    @Transactional
    public String getToken()
    {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();

        map.put("imp_key", PrivatePaymentIF.imp_key);
        map.put("imp_secret", PrivatePaymentIF.imp_secret);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        HttpEntity<String> entity = new HttpEntity<>(json, httpHeaders);
            return restTemplate.postForObject("https://api.iamport.kr/users/getToken", entity, String.class);
    }

    @Transactional
    public String CancelLogic(String merchant_uid)
    {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();

        map.put("merchant_uid", merchant_uid);
        map.put("amount", 1);

        Gson gson = new Gson();
        String json = gson.toJson(map);

        HttpEntity<String> entity = new HttpEntity<>(json, httpHeaders);
        return restTemplate.postForObject("https://api.iamport.kr/payments/cancel", entity, String.class);
    }

    // 빌링키 카카오 페이
    @Transactional
    public String RequestTokenKaKaoInfo(OrderInfoDto.KaKaoInfo kaKaoInfo, String customer_uid)
    {
        String token = getToken();

        Gson tokenInfo = new Gson();

        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);


        TokenDto tokenDto = tokenInfo.fromJson(token, TokenDto.class);

        String access_token = tokenDto.getAccess_token();
        log.info("access_token : " + access_token);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        Map<String, Object> map = new HashMap<>();
        map.put("customer_uid", kaKaoInfo.getCustomer_uid());
        map.put("merchant_uid", kaKaoInfo.getMerchant_uid());

        Gson var = new Gson();
        String json = var.toJson(map);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        return restTemplate.postForObject("https://api.iamport.kr/subscribe/customers/" + customer_uid, entity, String.class);
    }

    @Transactional
    public String RequestTokenInfo(OrderInfoDto.CardInfoList cardInfoList, String customer_uid)
    {
        String token = getToken();

        Gson tokenInfo = new Gson();

        token = token.substring(token.indexOf("response") + 10);
        token = token.substring(0, token.length() - 1);


        TokenDto tokenDto = tokenInfo.fromJson(token, TokenDto.class);

        String access_token = tokenDto.getAccess_token();
        log.info("access_token : " + access_token);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(access_token);

        Map<String, Object> map = new HashMap<>();
        map.put("customer_uid", cardInfoList.getCustomer_uid());
        map.put("card_number", cardInfoList.getCard_number());
        map.put("birth", cardInfoList.getBirth());
        map.put("pwd_2digit", cardInfoList.getPwd_2digit());
        map.put("customer_uid", cardInfoList.getCustomer_uid());

        Gson var = new Gson();
        String json = var.toJson(map);
        System.out.println(json);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        return restTemplate.postForObject("https://api.iamport.kr/subscribe/customers/" + customer_uid, entity, String.class);
    }

    // 빌링키 정보 받아오기
    @Transactional
    public BillikngKeyDto getBillingkey(OrderInfoDto.CardInfoList cardInfoList, String customer_uid)
    {
        String billingKey = RequestTokenInfo(cardInfoList, customer_uid);

        log.info("billing key : " + billingKey);
        Gson billingKeyInfo = new Gson();

        BillikngKeyDto billikngKeyDto = billingKeyInfo.fromJson(billingKey, BillikngKeyDto.class);

        log.info("billing key code : " + billikngKeyDto.getCode());

        return billikngKeyDto;
    }
}
