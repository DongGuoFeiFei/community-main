package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_session_member")
@ApiModel("会话成员实体")
public class ChatSessionMember {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("成员记录ID")
    private Long id;

    @ApiModelProperty("所属会话ID")
    private Long sessionId;

    @ApiModelProperty("成员用户ID")
    private Long userId;

    @ApiModelProperty("成员昵称")
    private String nickname;

    @ApiModelProperty("角色：1-普通 2-管理员 3-群主")
    private Integer role;

    @ApiModelProperty("加入状态：1-正常 2-被移除 3-待确认")
    private Integer joinStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("加入时间")
    private LocalDateTime joinTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("免打扰截止时间")
    private LocalDateTime muteUntil;

    @ApiModelProperty("系统记录的未读起始序列")
    private Long unreadSeq;

    @ApiModelProperty("最新已读序列号")
    private Long readSeq;

    @ApiModelProperty("是否存在未处理的@提醒")
    private Integer mentionFlag;

    @TableLogic
    @ApiModelProperty("逻辑删除标志")
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
