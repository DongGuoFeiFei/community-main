package com.example.communityserver.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * <p>
 * WebSocket配置类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler());
        // TODO: 2025/8/27 解决SockJS添加失败问题，SockJS是WebSocket解决旧浏览器无法连接问题得解决方案
    }

    // 消息代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 基于内存的消息代理
        registry.enableSimpleBroker("/topic", "/queue", "/user");// 服务端广播前缀
        registry.setApplicationDestinationPrefixes("/app");// 客户端发送前缀
        registry.setUserDestinationPrefix("/user");//设置用户目标的前缀
        // 基于rabbitMQ的消息代理
    }

//    //    原生 WebSocket API
//    @Bean
//    @ConditionalOnMissingBean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}
