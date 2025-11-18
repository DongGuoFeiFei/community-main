package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message_ack")
@ApiModel("消息已读状态实体")
public class ChatMessageAck {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键ID（可用雪花）")
    private Long id;

    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("已读的最大序列号")
    private Long readSeq;

    @ApiModelProperty("最后已读消息ID（兼容旧逻辑）")
    private Long lastMsgId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("最近已读时间")
    private LocalDateTime lastReadAt;

    @ApiModelProperty("设备标识：0-默认 1-移动端 2-PC 等")
    private Integer deviceFlag;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
