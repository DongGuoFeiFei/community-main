package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.request.MessageReadRequest;
import com.example.communityserver.chat.entity.response.MessagePageResponse;
import com.example.communityserver.chat.service.ChatMessageAckService;
import com.example.communityserver.chat.service.ChatMessageService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 聊天室
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-28
 **/

@Slf4j
@Api(tags = "聊天室")
@RestController
@RequestMapping("chat")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatMessageAckService chatMessageAckService;

    public ChatController(ChatMessageService chatMessageService,
                          ChatMessageAckService chatMessageAckService) {
        this.chatMessageService = chatMessageService;
        this.chatMessageAckService = chatMessageAckService;
    }

    @ApiOperation("发送消息")
    @PostMapping("messages")
    public Result<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        chatMessageService.save(message);
        return Result.success(message);
    }

    @ApiOperation("根据会话ID获取消息列表")
    @GetMapping("sessions/{sessionId}/messages")
    public Result<List<ChatMessage>> listMessages(@PathVariable Long sessionId) {
        List<ChatMessage> messages = chatMessageService.lambdaQuery()
                .eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getMsgSeq)
                .list();
        return Result.success(messages);
    }

    @ApiOperation("根据消息ID获取单条消息")
    @GetMapping("messages/{id}")
    public Result<ChatMessage> getMessage(@PathVariable Long id) {
        return Result.success(chatMessageService.getById(id));
    }

    @ApiOperation("撤回消息")
    @DeleteMapping("messages/{id}")
    public Result<Boolean> recallMessage(@PathVariable Long id) {
        return Result.success(chatMessageService.removeById(id));
    }

    @ApiOperation("分页获取会话消息")
    @GetMapping("messages")
    public Result<MessagePageResponse> getMessages(@RequestParam Long sessionId,
                                                   @RequestParam(required = false) Long lastSeq,
                                                   @RequestParam(required = false) Integer pageSize) {
        return Result.success(chatMessageService.getMessagePage(sessionId, lastSeq, pageSize));
    }

    @ApiOperation("标记消息已读")
    @PostMapping("messages/read")
    public Result<Boolean> markMessageAsRead(@RequestBody @Valid MessageReadRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        if (userId == null) {
            return Result.error("请先登录");
        }
        boolean success = chatMessageAckService.markMessagesAsRead(request.getSessionId(), userId, request.getReadSeq());
        return Result.success(success);
    }

}
