package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatMessageAck;
import com.example.communityserver.chat.service.ChatMessageAckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "消息已读管理")
@RestController
@RequestMapping("chat/message-acks")
public class ChatMessageAckController {

    private final ChatMessageAckService chatMessageAckService;

    public ChatMessageAckController(ChatMessageAckService chatMessageAckService) {
        this.chatMessageAckService = chatMessageAckService;
    }

    @ApiOperation("新增已读记录")
    @PostMapping
    public ChatMessageAck create(@RequestBody ChatMessageAck ack) {
        chatMessageAckService.save(ack);
        return ack;
    }

    @ApiOperation("按会话和用户获取已读记录")
    @GetMapping
    public List<ChatMessageAck> list(@RequestParam(required = false) Long sessionId,
                                     @RequestParam(required = false) Long userId) {
        return chatMessageAckService.lambdaQuery()
                .eq(sessionId != null, ChatMessageAck::getSessionId, sessionId)
                .eq(userId != null, ChatMessageAck::getUserId, userId)
                .list();
    }

    @ApiOperation("删除已读记录")
    @DeleteMapping("{id}")
    public Boolean delete(@PathVariable Long id) {
        return chatMessageAckService.removeById(id);
    }
}


