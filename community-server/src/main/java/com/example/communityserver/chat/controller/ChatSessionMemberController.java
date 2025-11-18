package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatSessionMember;
import com.example.communityserver.chat.service.ChatSessionMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "会话成员管理")
@RestController
@RequestMapping("chat/session-members")
public class ChatSessionMemberController {

    private final ChatSessionMemberService chatSessionMemberService;

    public ChatSessionMemberController(ChatSessionMemberService chatSessionMemberService) {
        this.chatSessionMemberService = chatSessionMemberService;
    }

    @ApiOperation("添加成员")
    @PostMapping
    public ChatSessionMember create(@RequestBody ChatSessionMember member) {
        chatSessionMemberService.save(member);
        return member;
    }

    @ApiOperation("按会话查询成员")
    @GetMapping
    public List<ChatSessionMember> list(@RequestParam(required = false) Long sessionId) {
        return chatSessionMemberService.lambdaQuery()
                .eq(sessionId != null, ChatSessionMember::getSessionId, sessionId)
                .list();
    }

    @ApiOperation("移除成员")
    @DeleteMapping("{id}")
    public Boolean delete(@PathVariable Long id) {
        return chatSessionMemberService.removeById(id);
    }
}
