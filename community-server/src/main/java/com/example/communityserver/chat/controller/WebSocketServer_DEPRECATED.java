package com.example.communityserver.chat.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * WebSocket服务器端点
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/

@Slf4j
@Component
@ServerEndpoint(value = "/api/websocket/{user_id}")
public class WebSocketServer_DEPRECATED {

    // 存储所有连接的会话
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> userSessionMap = new ConcurrentHashMap<>();

    private String userId;
    private Session session;

    // 获取在线用户数
    public static int getOnlineCount() {
        return sessions.size();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("user_id") String userId) {
        log.info("WebSocket连接建立, 用户ID: {}", userId);
        this.userId = userId;
        this.session = session;

        // 存储会话
        sessions.put(session.getId(), session);
        userSessionMap.put(userId, session.getId());

        // 通知所有用户有新用户加入
        broadcastSystemMessage(userId + " 加入了聊天室");

        // 发送当前在线用户列表给新连接的用户
        sendOnlineUsers(session);
    }

    @OnClose
    public void onClose() {
        log.info("WebSocket连接关闭, 用户ID: {}", userId);

        // 移除会话
        sessions.remove(session.getId());
        userSessionMap.remove(userId);

        // 通知所有用户有用户离开
        broadcastSystemMessage(userId + " 离开了聊天室");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自用户 {} 的消息: {}", userId, message);

        try {
            // 解析消息
            JSONObject jsonMessage = JSON.parseObject(message);
            String type = jsonMessage.getString("type");
            String content = jsonMessage.getString("content");
            String targetUserId = jsonMessage.getString("targetUserId");

            if ("chat".equals(type)) {
                if (targetUserId != null && !targetUserId.isEmpty()) {
                    // 私聊消息
                    sendPrivateMessage(userId, targetUserId, content);
                } else {
                    // 群聊消息
                    broadcastChatMessage(userId, content);
                }
            }
        } catch (Exception e) {
            log.error("消息解析错误: {}", e.getMessage());
            // 如果是普通文本消息，按群聊处理
            broadcastChatMessage(userId, message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket错误: {}", error.getMessage());
    }

    // 广播系统消息
    private void broadcastSystemMessage(String message) {
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("type", "system");
        systemMsg.put("content", message);
        systemMsg.put("timestamp", System.currentTimeMillis());

        broadcastToAll(systemMsg.toJSONString());
    }

    // 广播聊天消息
    private void broadcastChatMessage(String senderId, String message) {
        JSONObject chatMsg = new JSONObject();
        chatMsg.put("type", "chat");
        chatMsg.put("senderId", senderId);
        chatMsg.put("content", message);
        chatMsg.put("timestamp", System.currentTimeMillis());

        broadcastToAll(chatMsg.toJSONString());
    }

    // 发送私聊消息
    private void sendPrivateMessage(String senderId, String targetUserId, String message) {
        String targetSessionId = userSessionMap.get(targetUserId);
        if (targetSessionId != null) {
            Session targetSession = sessions.get(targetSessionId);
            if (targetSession != null && targetSession.isOpen()) {
                JSONObject privateMsg = new JSONObject();
                privateMsg.put("type", "private");
                privateMsg.put("senderId", senderId);
                privateMsg.put("content", message);
                privateMsg.put("timestamp", System.currentTimeMillis());

                try {
                    targetSession.getBasicRemote().sendText(privateMsg.toJSONString());

                    // 同时给发送者回显
                    Session senderSession = sessions.get(userSessionMap.get(senderId));
                    if (senderSession != null && senderSession.isOpen()) {
                        senderSession.getBasicRemote().sendText(privateMsg.toJSONString());
                    }
                } catch (IOException e) {
                    log.error("发送私聊消息失败: {}", e.getMessage());
                }
            }
        }
    }

    // 发送在线用户列表
    private void sendOnlineUsers(Session session) {
        JSONObject usersMsg = new JSONObject();
        usersMsg.put("type", "users");
        usersMsg.put("users", new ArrayList<>(userSessionMap.keySet()));
        usersMsg.put("timestamp", System.currentTimeMillis());

        try {
            session.getBasicRemote().sendText(usersMsg.toJSONString());
        } catch (IOException e) {
            log.error("发送在线用户列表失败: {}", e.getMessage());
        }
    }

    // 广播给所有用户
    private void broadcastToAll(String message) {
        sessions.values().forEach(s -> {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("广播消息失败: {}", e.getMessage());
                }
            }
        });
    }
}
