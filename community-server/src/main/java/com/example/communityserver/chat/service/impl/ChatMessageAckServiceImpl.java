package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.model.ChatMessageAck;
import com.example.communityserver.chat.mapper.ChatMessageAckMapper;
import com.example.communityserver.chat.service.ChatMessageAckService;
import com.example.communityserver.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 消息已读状态 Service 实现
 */
@Service
@RequiredArgsConstructor
public class ChatMessageAckServiceImpl extends ServiceImpl<ChatMessageAckMapper, ChatMessageAck>
        implements ChatMessageAckService {

    private final ChatMessageService chatMessageService;

    @Override
    public boolean markMessagesAsRead(Long sessionId, Long userId, Long readSeq) {
        if (sessionId == null || userId == null || readSeq == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        ChatMessageAck ack = this.lambdaQuery()
                .eq(ChatMessageAck::getSessionId, sessionId)
                .eq(ChatMessageAck::getUserId, userId)
                .one();
        LocalDateTime now = LocalDateTime.now();
        Long lastMsgId = resolveLastMsgId(sessionId, readSeq);

        if (ack == null) {
            ChatMessageAck newAck = new ChatMessageAck();
            newAck.setSessionId(sessionId);
            newAck.setUserId(userId);
            newAck.setReadSeq(readSeq);
            newAck.setLastMsgId(lastMsgId);
            newAck.setLastReadAt(now);
            return this.save(newAck);
        }

        boolean needUpdate = ack.getReadSeq() == null || readSeq > ack.getReadSeq();
        if (needUpdate) {
            ack.setReadSeq(readSeq);
            ack.setLastMsgId(lastMsgId);
        }
        ack.setLastReadAt(now);
        return this.updateById(ack);
    }

    private Long resolveLastMsgId(Long sessionId, Long readSeq) {
        ChatMessage message = chatMessageService.lambdaQuery()
                .eq(ChatMessage::getSessionId, sessionId)
                .le(ChatMessage::getMsgSeq, readSeq)
                .orderByDesc(ChatMessage::getMsgSeq)
                .last("LIMIT 1")
                .one();
        return message != null ? message.getId() : null;
    }
}


