package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.response.MessagePageResponse;
import com.example.communityserver.chat.mapper.ChatMessageMapper;
import com.example.communityserver.chat.service.ChatMessageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 聊天消息 Service 实现
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
                implements ChatMessageService {

        private static final int DEFAULT_PAGE_SIZE = 20;
        private static final int MAX_PAGE_SIZE = 100;

        /**
         * 异步保存消息，用于 WebSocket 收到消息后后台持久化
         */
        @Override
        @Async
        public void asyncSaveMessage(ChatMessage message) {
                if (message.getSendTime() == null) {
                        message.setSendTime(LocalDateTime.now());
                }
                this.save(message);
        }

        @Override
        public MessagePageResponse getMessagePage(Long sessionId, Long lastSeq, Integer pageSize) {
                if (sessionId == null) {
                        throw new IllegalArgumentException("会话ID不能为空");
                }
                int size = pageSize == null ? DEFAULT_PAGE_SIZE : Math.max(1, Math.min(pageSize, MAX_PAGE_SIZE));
                int limit = size + 1;

                LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ChatMessage::getSessionId, sessionId);
                if (lastSeq != null) {
                        wrapper.gt(ChatMessage::getMsgSeq, lastSeq);
                }
                wrapper.orderByAsc(ChatMessage::getMsgSeq).last("LIMIT " + limit);

                List<ChatMessage> records = this.list(wrapper);
                if (records == null) {
                        records = Collections.emptyList();
                }
                boolean hasMore = records.size() > size;
                if (hasMore) {
                        records = records.subList(0, size);
                }
                Long nextSeq = records.isEmpty() ? null : records.get(records.size() - 1).getMsgSeq();

                MessagePageResponse response = new MessagePageResponse();
                response.setRecords(records);
                response.setHasMore(hasMore);
                response.setNextSeq(hasMore ? nextSeq : null);
                response.setCursor(hasMore ? nextSeq : null);
                response.setPrevSeq(lastSeq);
                return response;
        }

        @Override
        public ChatMessage findBySessionAndSeq(Long sessionId, Long msgSeq) {
                if (sessionId == null || msgSeq == null) {
                        return null;
                }
                return this.lambdaQuery()
                                .eq(ChatMessage::getSessionId, sessionId)
                                .eq(ChatMessage::getMsgSeq, msgSeq)
                                .one();
        }
}
