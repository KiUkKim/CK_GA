package com.ck.reusable.springboot.web.Test;

import com.ck.reusable.springboot.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public UserWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendUpdateMessage(String userEmail, User user) {
        messagingTemplate.convertAndSend("/topic/user/" + userEmail, user);
    }
}
