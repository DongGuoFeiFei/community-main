package com.example.communityserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * <p>
 * WebSocket 配置类
 * 配置 STOMP 消息代理和端点
 * </p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册 STOMP 端点
     * 客户端通过此端点建立 WebSocket 连接
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                // 允许跨域访问
                .setAllowedOriginPatterns("*");
                // 添加 SockJS 支持（用于不支持 WebSocket 的浏览器）
                // .withSockJS();
        
        // 注意：SockJS 会改变连接方式，如果前端使用原生 WebSocket，不要启用 SockJS
        // 如果需要兼容旧浏览器，可以取消上面的注释
    }

    /**
     * 配置消息代理
     * 定义消息的路由规则
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用简单的内存消息代理
        // /topic - 用于广播消息（一对多）
        // /queue - 用于点对点消息（一对一）
        // /user - 用于用户特定消息
        registry.enableSimpleBroker("/topic", "/queue", "/user");
        
        // 设置客户端发送消息的目标前缀
        // 客户端发送到 /app/xxx 的消息会被路由到 @MessageMapping("xxx") 的方法
        registry.setApplicationDestinationPrefixes("/app");
        
        // 设置用户目标的前缀
        // 用于点对点消息，例如 /user/{username}/queue/messages
        registry.setUserDestinationPrefix("/user");
        
        // 如果需要使用 RabbitMQ 等外部消息代理，可以配置如下：
        // registry.enableStompBrokerRelay("/topic", "/queue")
        //         .setRelayHost("localhost")
        //         .setRelayPort(61613)
        //         .setClientLogin("guest")
        //         .setClientPasscode("guest");
    }
}
