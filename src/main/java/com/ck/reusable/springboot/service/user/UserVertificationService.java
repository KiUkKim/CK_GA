package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.PrivateInterFace.PrivateIF;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserVertificationService {

    public String ckReusableAppNumCheck(String to){
        String api_key = PrivateIF.api_key;
        String api_secret = PrivateIF.api_secret;
        String api_url = "https://api.coolsms.co.kr";

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret, api_url);

        Message message = new Message();

        Random rand = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        message.setFrom(PrivateIF.call_num);
        message.setTo(to);
        message.setText("충북대학교 리유저블 앱 인증번호는 [" + numStr + "] 입니다.");

        messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(numStr);

        return numStr;
    }
}
