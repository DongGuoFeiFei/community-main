package com.example.communityserver.chat.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-03
 **/

@Slf4j
@Component
public class WebSocketEventListener {

    public static Integer userNumber = 0;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        userNumber++;
        messagingTemplate.convertAndSend("/topic/join", userNumber);
        log.info("我来了哦~");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        userNumber--;
        messagingTemplate.convertAndSend("/topic/leave", userNumber);
        log.info("我走了哦~");
    }
}
