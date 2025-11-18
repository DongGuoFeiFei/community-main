package com.example.communityserver.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.chat.entity.model.ChatMessageAck;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息已读状态 Mapper
 */
@Mapper
public interface ChatMessageAckMapper extends BaseMapper<ChatMessageAck> {
}
