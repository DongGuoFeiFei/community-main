package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatSession;
import com.example.communityserver.chat.service.ChatSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "会话管理")
@RestController
@RequestMapping("chat/sessions")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @ApiOperation("创建会话")
    @PostMapping
    public ChatSession create(@RequestBody ChatSession session) {
        chatSessionService.save(session);
        return session;
    }

    @ApiOperation("会话详情")
    @GetMapping("{id}")
    public ChatSession detail(@PathVariable Long id) {
        return chatSessionService.getById(id);
    }

    @ApiOperation("会话列表（简单示例：查全部）")
    @GetMapping
    public List<ChatSession> list() {
        return chatSessionService.list();
    }

    @ApiOperation("更新会话")
    @PutMapping("{id}")
    public Boolean update(@PathVariable Long id, @RequestBody ChatSession session) {
        session.setId(id);
        return chatSessionService.updateById(session);
    }

    @ApiOperation("删除会话")
    @DeleteMapping("{id}")
    public Boolean delete(@PathVariable Long id) {
        return chatSessionService.removeById(id);
    }
}


