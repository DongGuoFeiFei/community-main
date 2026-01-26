package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.request.MarkReadRequest;
import com.example.communityserver.chat.entity.request.SendMessageRequest;
import com.example.communityserver.chat.entity.response.MessagePageResponse;
import com.example.communityserver.chat.service.ChatMessageService;
import com.example.communityserver.chat.service.ChatSessionMemberService;
import com.example.communityserver.chat.service.ChatSessionService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 聊天消息 HTTP 控制器
 * 提供 HTTP REST API 用于消息管理（历史查询、标记已读等）
 * 
 * 注意：实时消息发送和撤回请使用 WebSocket（WebSocketController）
 * 此控制器主要用于：
 * 1. 历史消息查询
 * 2. 消息已读标记
 * 3. WebSocket 不可用时的降级方案
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Slf4j
@Api(tags = "聊天消息管理")
@RestController
@RequestMapping("/chat/sessions/{sessionId}/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService messageService;
    private final ChatSessionService sessionService;
    private final ChatSessionMemberService memberService;

    @ApiOperation("获取消息列表（分页）")
    @GetMapping
    public Result<MessagePageResponse> getMessages(
            @ApiParam("会话ID") @PathVariable Long sessionId,
            @ApiParam("上一页最后一条消息ID") @RequestParam(required = false) Long lastMsgId,
            @ApiParam("每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {

        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        // 验证用户是否是会话成员
        if (!sessionService.isMember(sessionId, currentUserId)) {
            return Result.error("您不是该会话的成员");
        }

        MessagePageResponse response = messageService.getMessages(sessionId, lastMsgId, pageSize);
        return Result.success(response);
    }

    @ApiOperation("发送消息（HTTP 方式，推荐使用 WebSocket）")
    @PostMapping
    public Result<ChatMessage> sendMessage(
            @ApiParam("会话ID") @PathVariable Long sessionId,
            @RequestBody @Valid SendMessageRequest request) {

        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        try {
            // 注意：HTTP 方式发送消息不会实时推送给其他用户
            // 推荐使用 WebSocket 发送消息以获得更好的实时性
            ChatMessage message = messageService.sendMessage(sessionId, currentUserId, request);
            
            log.info("通过 HTTP 发送消息: sessionId={}, messageId={}, senderId={}", 
                    sessionId, message.getId(), currentUserId);
            
            return Result.success(message);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("撤回消息（HTTP 方式，推荐使用 WebSocket）")
    @DeleteMapping("/{messageId}")
    public Result<Boolean> recallMessage(
            @ApiParam("会话ID") @PathVariable Long sessionId,
            @ApiParam("消息ID") @PathVariable Long messageId) {

        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        try {
            // 注意：HTTP 方式撤回消息不会实时通知其他用户
            // 推荐使用 WebSocket 撤回消息以实时通知会话内所有成员
            boolean success = messageService.recallMessage(messageId, currentUserId);
            
            log.info("通过 HTTP 撤回消息: sessionId={}, messageId={}, userId={}", 
                    sessionId, messageId, currentUserId);
            
            return Result.success(success);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("标记已读")
    @PostMapping("/read")
    public Result<Boolean> markAsRead(
            @ApiParam("会话ID") @PathVariable Long sessionId,
            @RequestBody @Valid MarkReadRequest request) {

        Long currentUserId = SecurityUtils.getLoginUserId();
        if (currentUserId == null) {
            return Result.error("请先登录");
        }

        // 验证用户是否是会话成员
        if (!sessionService.isMember(sessionId, currentUserId)) {
            return Result.error("您不是该会话的成员");
        }

        boolean success = memberService.markAsRead(sessionId, currentUserId, request.getMessageId());
        return Result.success(success);
    }
}
