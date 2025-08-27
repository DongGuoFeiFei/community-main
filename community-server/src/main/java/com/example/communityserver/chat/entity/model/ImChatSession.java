package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("im_chat_session")
@ApiModel(value = "聊天会话实体类")
public class ImChatSession {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "会话ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "会话类型：1-私聊，2-群聊", example = "1")
    private Integer type;

    @ApiModelProperty(value = "会话名称", example = "技术交流群")
    private String name;

    @ApiModelProperty(value = "会话头像URL")
    private String avatar;

    @ApiModelProperty(value = "最后一条消息ID", example = "10086")
    private Long lastMessageId;

    @ApiModelProperty(value = "最后一条消息内容预览", example = "大家好，我是新成员")
    private String lastMessageContent;

    @ApiModelProperty(value = "最后一条消息时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMessageTime;

    @ApiModelProperty(value = "最后一条消息发送者ID", example = "456")
    private Long lastMessageUserId;

    @ApiModelProperty(value = "成员数量", example = "10")
    private Integer memberCount;

    @ApiModelProperty(value = "逻辑删除标志", example = "0")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
