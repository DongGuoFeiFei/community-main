package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ImChatSession;
import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.mapper.ImChatSessionMapper;
import com.example.communityserver.chat.mapper.ImMessageMapper;
import com.example.communityserver.chat.service.IImChatSessionService;
import com.example.communityserver.chat.service.IImMessageService;
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
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements IImMessageService {
}
