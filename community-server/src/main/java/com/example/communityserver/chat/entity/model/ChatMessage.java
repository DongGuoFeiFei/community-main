package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.example.communityserver.chat.entity.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message")
@ApiModel("会话消息实体")
public class ChatMessage {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("全局消息ID")
    private Long id;

    @TableField("session_id")
    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("会话内递增序列号")
    private Long msgSeq;

    @ApiModelProperty("发送者ID")
    private Long senderId;

    @ApiModelProperty("发送者昵称快照")
    private String senderName;

    @ApiModelProperty("发送者头像快照")
    private String senderAvatar;

    @ApiModelProperty("消息类型：1文本 2图片 3文件 4音频 5视频 6系统")
    private Integer contentType;

    @ApiModelProperty("消息内容/资源地址")
    private String content;

    @ApiModelProperty("富文本/附件扩展（JSON）")
    private String contentJson;

    @ApiModelProperty("@用户ID集合（JSON）")
    private String atUserIds;

    @ApiModelProperty("引用的消息ID")
    private Long quoteMsgId;

    @ApiModelProperty("附件大小（字节）")
    private Long fileSize;

    @ApiModelProperty("消息状态：1正常 2撤回 3屏蔽")
    private Integer status;

    @TableLogic
    @ApiModelProperty("逻辑删除标志")
    private Integer isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("客户端发送时间")
    private LocalDateTime sendTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
