package com.example.communityserver.chat.config;

import com.example.communityserver.core.security.util.JWTUtil;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * <p>
 * WebSocket 安全配置
 * 集成现有的 JWT 验证机制，确保 WebSocket 连接的安全性
 * </p>
 *
 * @author DongGuo
 * @create 2026-01-23
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ExecutorChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    // 处理 CONNECT 命令 - 验证 token 并存储用户信息
                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        authenticateConnection(accessor);
                    }
                }

                return message;
            }
            
            /**
             * 在消息处理线程执行前调用
             * 这个方法会在实际的消息处理线程中执行，所以可以设置 SecurityContext
             * 
             * 关键：这里设置的 SecurityContext 会在消息处理方法（@MessageMapping）中生效
             */
            @Override
            public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (accessor != null) {
                    // 从会话属性恢复认证信息到当前线程的 SecurityContext
                    Authentication authentication = (Authentication) accessor.getSessionAttributes().get("authentication");
                    
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        // 只在 SEND 命令时记录详细日志（实际发送消息）
                        if (StompCommand.SEND.equals(accessor.getCommand())) {
                            Long userId = (Long) accessor.getSessionAttributes().get("userId");
                            log.debug("处理消息 - 用户ID: {}, 目的地: {}, 线程: {}", 
                                    userId, accessor.getDestination(), Thread.currentThread().getName());
                        }
                    } else {
                        // 认证失败时才记录警告
                        if (StompCommand.SEND.equals(accessor.getCommand())) {
                            log.warn("无法恢复认证信息 - 会话ID: {}, 命令: {}", 
                                    accessor.getSessionId(), accessor.getCommand());
                        }
                    }
                }
                
                return message;
            }
            
            /**
             * 在消息处理完成后调用
             * 清除 SecurityContext，防止线程池复用时的污染
             */
            @Override
            public void afterMessageHandled(Message<?> message, MessageChannel channel, 
                                           MessageHandler handler, Exception ex) {
                // 清除 SecurityContext（不记录日志，避免日志过多）
                SecurityContextHolder.clearContext();
            }
            
            /**
             * 验证 WebSocket 连接并存储用户信息
             */
            private void authenticateConnection(StompHeaderAccessor accessor) {
                // 从请求头中获取 token
                String token = accessor.getFirstNativeHeader("Authorization");
                
                log.info("WebSocket 连接请求 - 会话ID: {}, Token: {}", 
                        accessor.getSessionId(), 
                        token != null ? "已提供" : "未提供");

                // 验证 token
                if (token == null || token.isEmpty()) {
                    log.warn("WebSocket 连接被拒绝 - 未提供 Token");
                    throw new IllegalArgumentException("未提供访问令牌");
                }

                // 移除 "Bearer " 前缀（如果存在）
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                // 验证 token 有效性
                if (!JWTUtil.verifyToken(token)) {
                    log.warn("WebSocket 连接被拒绝 - Token 无效或已过期");
                    throw new IllegalArgumentException("访问令牌无效或已过期");
                }

                // 获取用户 ID
                Long userId = JWTUtil.getUserId(token);
                if (userId == null) {
                    log.warn("WebSocket 连接被拒绝 - 无法从 Token 中获取用户ID");
                    throw new IllegalArgumentException("无效的访问令牌");
                }

                // 从 Redis 获取用户信息
                LoginUser loginUser = redisUtil.getCacheObject(CacheKeyConstants.LOGIN_USER_ID + userId);
                if (loginUser == null) {
                    log.warn("WebSocket 连接被拒绝 - 用户登录信息已过期，用户ID: {}", userId);
                    throw new IllegalArgumentException("用户登录信息已过期，请重新登录");
                }

                // 创建认证对象
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        loginUser, null, loginUser.getAuthorities());

                // 将用户信息存储到 WebSocket 会话属性中（重要：用于后续消息处理）
                accessor.getSessionAttributes().put("userId", userId);
                accessor.getSessionAttributes().put("username", loginUser.getUsername());
                accessor.getSessionAttributes().put("loginUser", loginUser);
                accessor.getSessionAttributes().put("authentication", authentication);
                accessor.setUser(authentication);

                log.info("WebSocket 连接认证成功 - 用户ID: {}, 用户名: {}", userId, loginUser.getUsername());
            }
            
        });
    }
}
