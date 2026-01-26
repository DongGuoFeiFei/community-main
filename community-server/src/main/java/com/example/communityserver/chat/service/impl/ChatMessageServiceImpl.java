package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.enums.MessageStatus;
import com.example.communityserver.chat.entity.enums.MessageType;
import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.request.SendMessageRequest;
import com.example.communityserver.chat.entity.response.MessagePageResponse;
import com.example.communityserver.chat.mapper.ChatMessageMapper;
import com.example.communityserver.chat.service.ChatMessageService;
import com.example.communityserver.chat.service.ChatSessionService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.entity.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息服务实现
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
        implements ChatMessageService {

    private final ChatSessionService sessionService;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int RECALL_TIME_LIMIT_MINUTES = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessage sendMessage(Long sessionId, Long senderId, SendMessageRequest request) {
        // 1. 验证会话是否存在
        if (!sessionService.isMember(sessionId, senderId)) {
            throw new IllegalArgumentException("您不是该会话的成员");
        }

        // 2. 获取发送者信息
        User sender = SecurityUtils.getCurrentUserDetail();
        if (sender == null || !sender.getUserId().equals(senderId)) {
            throw new IllegalArgumentException("发送者信息不匹配");
        }

        // 3. 构建消息实体
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setSenderId(senderId);
        message.setSenderName(sender.getNickname() != null ? sender.getNickname() : sender.getUsername());
        message.setSenderAvatar(sender.getAvatar());
        message.setMsgType(request.getMsgType());
        message.setContent(request.getContent());
        message.setExtraData(request.getExtraData());
        message.setQuoteMsgId(request.getQuoteMsgId());
        message.setStatus(MessageStatus.NORMAL.getCode());
        message.setSendTime(LocalDateTime.now());

        // 4. 保存消息
        this.save(message);

        // 5. 更新会话最新消息
        String digest = getMessageDigest(message.getMsgType(), message.getContent());
        sessionService.updateLastMessage(
                sessionId,
                message.getId(),
                digest,
                senderId,
                message.getSendTime()
        );

        log.info("用户 {} 在会话 {} 发送了消息: {}", senderId, sessionId, message.getId());
        return message;
    }

    @Override
    public MessagePageResponse getMessages(Long sessionId, Long lastMsgId, Integer pageSize) {
        // 参数校验
        if (sessionId == null) {
            throw new IllegalArgumentException("会话ID不能为空");
        }

        // 限制分页大小
        int size = pageSize == null ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        int limit = size + 1; // 多查一条用于判断是否还有更多

        // 构建查询条件
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
                .eq(ChatMessage::getStatus, MessageStatus.NORMAL.getCode());

        // 如果有lastMsgId，则查询比它更早的消息
        if (lastMsgId != null) {
            wrapper.lt(ChatMessage::getId, lastMsgId);
        }

        // 按ID倒序查询（最新的在前）
        wrapper.orderByDesc(ChatMessage::getId)
                .last("LIMIT " + limit);

        List<ChatMessage> records = this.list(wrapper);

        // 判断是否还有更多
        boolean hasMore = records.size() > size;
        if (hasMore) {
            records = records.subList(0, size);
        }

        // 获取下一页的游标
        Long nextCursor = records.isEmpty() ? null : records.get(records.size() - 1).getId();

        // 构建响应
        MessagePageResponse response = new MessagePageResponse();
        response.setMessages(records);
        response.setHasMore(hasMore);
        response.setNextCursor(hasMore ? nextCursor : null);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recallMessage(Long messageId, Long userId) {
        // 1. 查询消息
        ChatMessage message = this.getById(messageId);
        if (message == null) {
            throw new IllegalArgumentException("消息不存在");
        }

        // 2. 验证是否是发送者
        if (!message.getSenderId().equals(userId)) {
            throw new IllegalArgumentException("只能撤回自己的消息");
        }

        // 3. 验证是否在撤回时间限制内
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sendTime = message.getSendTime();
        if (sendTime.plusMinutes(RECALL_TIME_LIMIT_MINUTES).isBefore(now)) {
            throw new IllegalArgumentException("消息发送超过2分钟，无法撤回");
        }

        // 4. 更新消息状态为已撤回
        message.setStatus(MessageStatus.RECALLED.getCode());
        boolean success = this.updateById(message);

        if (success) {
            log.info("用户 {} 撤回了消息: {}", userId, messageId);
        }

        return success;
    }

    @Override
    public String getMessageDigest(Integer msgType, String content) {
        MessageType type = MessageType.getByCode(msgType);
        if (type == null) {
            return "[未知消息]";
        }

        switch (type) {
            case TEXT:
                // 文本消息：截取前50个字符
                if (content == null || content.isEmpty()) {
                    return "[空消息]";
                }
                return content.length() > 50 ? content.substring(0, 50) + "..." : content;

            case IMAGE:
                return "[图片]";

            case FILE:
                return "[文件]";

            case SYSTEM:
                return content != null ? content : "[系统消息]";

            default:
                return "[未知消息]";
        }
    }
}
