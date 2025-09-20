package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ImChatSession;
import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.entity.response.SessionDetailVo;
import com.example.communityserver.chat.service.IImChatSessionService;
import com.example.communityserver.chat.service.IImMessageService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private IImChatSessionService chatSessionService;

    @Autowired
    private IImMessageService iImMessageService;

    @ApiOperation("获取会话列表")
    @GetMapping("sessions")
//    @RequiresPermission(api = {"chat:sessions:get"}, role = {"super_admin"}, logical = Logical.OR)
    public Result<List<ImChatSession>> getSessions() {
        List<ImChatSession> sessions = chatSessionService.getSessions();
        return Result.success(sessions);
    }

    @ApiOperation("获取会话消息")
    @GetMapping("sessions/{sessionId}/messages")
//    @RequiresPermission(api = {"chat:sessions:{id}:messages:get"}, role = {"super_admin"}, logical = Logical.OR)
    public Result<List<ImMessage>> getSessionMessages(@PathVariable Long sessionId) {
        List<ImMessage> sessions = chatSessionService.getSessionMessages(sessionId);
        return Result.success(sessions);
    }

    @ApiOperation("获取会话详情")
    @GetMapping("sessions/{sessionId}")
//    @RequiresPermission(api = {"chat:sessions:{id}:get"}, role = {"super_admin"}, logical = Logical.OR)
    public Result<SessionDetailVo> getSessionDetail(@PathVariable Long sessionId) {
        SessionDetailVo sessionDetailVo = chatSessionService.getSessionDetail(sessionId);
        return Result.success(sessionDetailVo);
    }

    @ApiOperation("获取会话消息历史")
    @GetMapping("messages")
//    @RequiresPermission(api = {"chat:messages:get"}, role = {"super_admin"}, logical = Logical.OR)
    public Result<List<ImMessage>> getMessages(Long sessionId, Long lastMessageId) {
        List<ImMessage> imMessages = iImMessageService.getMessages(sessionId, lastMessageId);
        return Result.success(imMessages);
    }

}
