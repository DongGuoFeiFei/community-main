package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话成员实体
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@TableName("chat_session_member")
@ApiModel("会话成员实体")
public class ChatSessionMember {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("角色：1-普通成员 2-管理员 3-群主")
    private Integer role;

    @ApiModelProperty("群昵称")
    private String nickname;

    @ApiModelProperty("最后已读消息ID")
    private Long lastReadMsgId;

    @ApiModelProperty("最后已读时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastReadTime;

    @ApiModelProperty("是否免打扰：0-否 1-是")
    private Integer isMuted;

    @ApiModelProperty("是否置顶：0-否 1-是")
    private Integer isPinned;

    @ApiModelProperty("状态：1-正常 2-已退出")
    private Integer status;

    @ApiModelProperty("加入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;

//    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
