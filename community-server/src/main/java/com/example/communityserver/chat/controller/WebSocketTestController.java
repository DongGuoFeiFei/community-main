package com.example.communityserver.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-27
 **/


@RestController
@RequestMapping("/api/websocket")
public class WebSocketTestController {

    @GetMapping("/test")
    public ResponseEntity<?> testWebSocketConfig() {
        return ResponseEntity.ok(Map.of(
                "status", "WebSocket endpoint configured",
                "endpoint", "/ws",
                "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuthentication(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "authenticated", userDetails != null,
                "username", userDetails != null ? userDetails.getUsername() : "anonymous",
                "authorities", userDetails != null ? userDetails.getAuthorities() : List.of()
        ));
    }
}
