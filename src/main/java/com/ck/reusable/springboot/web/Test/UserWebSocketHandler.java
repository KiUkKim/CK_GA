package com.ck.reusable.springboot.web.Test;

import com.ck.reusable.springboot.service.user.UserService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class UserWebSocketHandler extends TextWebSocketHandler {

    private UserService userService;

    private List<WebSocketSession> sessions = new ArrayList<>();

    public UserWebSocketHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // WebSocket으로 전달된 데이터 처리
        // 변경된 데이터가 있다면 sessions에 등록된 모든 session으로 해당 데이터를 전송합니다.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
