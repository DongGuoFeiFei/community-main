package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.entity.model.ImSessionMember;
import com.example.communityserver.chat.mapper.ImMessageMapper;
import com.example.communityserver.chat.mapper.ImSessionMemberMapper;
import com.example.communityserver.chat.service.IImMessageService;
import com.example.communityserver.chat.service.IImSessionMemberService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/


@Service
public class ImSessionMemberServiceImpl extends ServiceImpl<ImSessionMemberMapper, ImSessionMember> implements IImSessionMemberService {
}
