package com.example.communityserver.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.chat.entity.model.ImMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/


public interface ImMessageMapper extends BaseMapper<ImMessage> {
    @Insert("<script>" +
            "INSERT INTO im_message (session_id, sender_id, content, send_time) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.sessionId}, #{item.senderId}, #{item.content}, #{item.sendTime})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("list") List<ImMessage> messages);
}
