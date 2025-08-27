package com.example.communityserver.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    System.out.println("=== STOMP 命令: " + accessor.getCommand());
                    System.out.println("=== 会话ID: " + accessor.getSessionId());
                    System.out.println("=== 原生头: " + accessor.getMessageHeaders());

                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        String token = accessor.getFirstNativeHeader("Authorization");
                        System.out.println("=== Token: " + (token != null ? "提供" : "未提供"));
                    }
                }
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
                // 注解放行
//                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String token = accessor.getFirstNativeHeader("Authorization");
//
//                    if (token != null && token.startsWith("Bearer ")) {
//                        token = token.substring(7);
//                        try {
//                            String username = SecurityUtils.getCurrentUsername();
//                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                            // todo 进一步验证token和个人的管理是否正确，而不仅仅是验证token本身的正确性
//                            if (JWTUtil.verifyToken(token)) {
//                                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                                SecurityContextHolder.getContext().setAuthentication(authentication);
//                                accessor.setUser(authentication);
//                                log.info("WebSocket user authenticated: {}", username);
//                            }
//                        } catch (Exception e) {
//                            log.error("WebSocket authentication failed: {}", e.getMessage());
//                            throw new RuntimeException("Authentication failed");
//                        }
//                    }
//                }
                return message;
            }

        });
    }

}
