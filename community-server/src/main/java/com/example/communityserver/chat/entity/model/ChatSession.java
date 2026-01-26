package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天会话实体
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@TableName("chat_session")
@ApiModel("聊天会话实体")
public class ChatSession {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("会话类型：1-私聊 2-群聊")
    private Integer type;

    @ApiModelProperty("会话名称（群聊用）")
    private String name;

    @ApiModelProperty("会话头像（群聊用）")
    private String avatar;

    @ApiModelProperty("最新消息ID")
    private Long lastMsgId;

    @ApiModelProperty("最新消息内容摘要")
    private String lastMsgContent;

    @ApiModelProperty("最新消息时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMsgTime;

    @ApiModelProperty("最新消息发送者ID")
    private Long lastMsgSenderId;

    @ApiModelProperty("状态：1-正常 2-已删除")
    private Integer status;

//    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
