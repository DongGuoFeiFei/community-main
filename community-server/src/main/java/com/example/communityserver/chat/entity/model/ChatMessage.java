package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@TableName("chat_message")
@ApiModel("聊天消息实体")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("消息ID")
    private Long id;

    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("发送者ID")
    private Long senderId;

    @ApiModelProperty("发送者昵称（快照）")
    private String senderName;

    @ApiModelProperty("发送者头像（快照）")
    private String senderAvatar;

    @ApiModelProperty("消息类型：1-文本 2-图片 3-文件 4-系统")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("扩展数据（JSON格式）")
    private String extraData;

    @ApiModelProperty("引用的消息ID")
    private Long quoteMsgId;

    @ApiModelProperty("状态：1-正常 2-已撤回 3-已删除")
    private Integer status;

    @ApiModelProperty("发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

//    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
