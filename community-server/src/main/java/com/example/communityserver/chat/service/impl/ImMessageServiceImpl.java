package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.mapper.ImMessageMapper;
import com.example.communityserver.chat.service.IImMessageService;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 聊天室消息表 服务实现类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/

@Slf4j
@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements IImMessageService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ImMessageMapper messageMapper;

    @Override
    public List<ImMessage> getMessages(Long sessionId, Long lastMessageId) {

        // TODO: 2025/9/19 日后分库分表时，要根据日期寻找对应的库表
        List<ImMessage> imMessages = new ArrayList<>();
        // redis -- mysql
        if (lastMessageId == null) {
            String redisKey = CacheKeyConstants.CHAT_SESSION_MESSAGES + sessionId;
            imMessages = redisUtil.getCacheList(redisKey);
        }
        if (!imMessages.isEmpty()) {
            return imMessages;
        }

        if (lastMessageId != null) {
            ImMessage imMessage = messageMapper.selectOne(
                    new LambdaQueryWrapper<ImMessage>()
                            .eq(ImMessage::getId, lastMessageId)
            );

            if (imMessage != null) {
                return messageMapper.selectList(
                        new LambdaQueryWrapper<ImMessage>()
                                .lt(ImMessage::getCreateTime, imMessage.getCreateTime())
                                .orderByDesc(ImMessage::getId)
                                .last("LIMIT 30")
                );
            }
        }

        // 如果 lastMessageId 为 null 或者找不到对应的消息，则查询最新的200条消息
        return messageMapper.selectList(
                new LambdaQueryWrapper<ImMessage>()
                        .orderByDesc(ImMessage::getId)
                        .last("LIMIT 200")
        );
    }
}
