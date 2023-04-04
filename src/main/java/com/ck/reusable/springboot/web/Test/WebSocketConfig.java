package com.ck.reusable.springboot.web.Test;


import com.ck.reusable.springboot.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.sockjs.transport.handler.DefaultSockJsService;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }/*클라이언트가 웹 소켓 서버에 연결하는데 사용할 웹 소켓 엔드포인트 등록
    withSockJS를 통해 웹 소켓을 지원하지 않는 브라우저에 대해 웹 소켓을 대체한다.
	  +)메소드명에 STOMP가 들어가는 경우 통신 프로토콜인 STOMP구현에서 작동된다. */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 해당 이름으로 요청을 한 구독자 모두에게 brodcast한다.
        registry.setApplicationDestinationPrefixes("/app"); // 해당주소로 시작되는 메시지를 message-handling method로 라우팅
    }
}
