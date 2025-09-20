package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ImMessage;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/


public interface IImMessageService extends IService<ImMessage> {
    List<ImMessage> getMessages(Long sessionId, Long lastMessageId);
}
