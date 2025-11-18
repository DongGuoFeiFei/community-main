package com.example.communityserver.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.chat.entity.model.ChatSessionMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话成员 Mapper
 */
@Mapper
public interface ChatSessionMemberMapper extends BaseMapper<ChatSessionMember> {
}
