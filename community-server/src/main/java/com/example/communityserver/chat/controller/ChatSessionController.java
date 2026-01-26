package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.request.CreatePrivateSessionRequest;
import com.example.communityserver.chat.entity.response.SessionDetailResponse;
import com.example.communityserver.chat.entity.response.SessionListItemResponse;
import com.example.communityserver.chat.service.ChatSessionService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 聊天会话控制器
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Slf4j
@Api(tags = "聊天会话")
@RestController
@RequestMapping("/chat/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService sessionService;

    @ApiOperation("获取会话列表")
    @GetMapping
    public Result<List<SessionListItemResponse>> getSessions() {
        Long userId = SecurityUtils.getLoginUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }

        List<SessionListItemResponse> sessions = sessionService.getUserSessions(userId);
        return Result.success(sessions);
    }

    @ApiOperation("创建私聊会话")
    @PostMapping("/private")
    public Result<Long> createPrivateSession(@RequestBody @Valid CreatePrivateSessionRequest request) {
        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        Long sessionId = sessionService.createOrGetPrivateSession(currentUserId, request.getUserId());
        return Result.success(sessionId);
    }

    @ApiOperation("获取会话详情")
    @GetMapping("/{sessionId}")
    public Result<SessionDetailResponse> getSessionDetail(@PathVariable Long sessionId) {
        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        SessionDetailResponse detail = sessionService.getSessionDetail(sessionId, currentUserId);
        return Result.success(detail);
    }

    @ApiOperation("删除会话")
    @DeleteMapping("/{sessionId}")
    public Result<Boolean> deleteSession(@PathVariable Long sessionId) {
        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        boolean success = sessionService.deleteSession(sessionId, currentUserId);
        return Result.success(success);
    }
}
