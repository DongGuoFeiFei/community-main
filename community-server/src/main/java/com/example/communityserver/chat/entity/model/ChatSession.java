package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_session")
@ApiModel("聊天会话实体")
public class ChatSession {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("会话类型：1-私聊 2-群聊 3-公开")
    private Integer type;

    @ApiModelProperty("业务类型扩展：0-默认 1-活动 2-客服等")
    private Integer bizType;

    @ApiModelProperty("会话名称")
    private String name;

    @ApiModelProperty("会话头像")
    private String avatar;

    @ApiModelProperty("会话所有者/群主ID")
    private Long ownerId;

    @ApiModelProperty("最新消息ID")
    private Long lastMsgId;

    @ApiModelProperty("最新消息序列号")
    private Long lastMsgSeq;

    @ApiModelProperty("最新消息摘要")
    private String lastMsgDigest;

    @ApiModelProperty("最新消息发送者ID")
    private Long lastMsgUserId;

    @ApiModelProperty("成员数量")
    private Integer memberCount;

    @ApiModelProperty("会话状态：1-正常 2-封禁 3-解散")
    private Integer status;

    @TableLogic
    @ApiModelProperty("逻辑删除：0-正常 1-删除")
    private Integer isDeleted;

    @ApiModelProperty("扩展字段（JSON）")
    private String ext;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
