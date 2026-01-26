package com.example.communityserver.chat.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话列表项响应
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("会话列表项")
public class SessionListItemResponse {

    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("会话类型：1-私聊 2-群聊")
    private Integer type;

    @ApiModelProperty("会话名称")
    private String name;

    @ApiModelProperty("会话头像")
    private String avatar;

    @ApiModelProperty("最新消息内容")
    private String lastMsgContent;

    @ApiModelProperty("最新消息时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMsgTime;

    @ApiModelProperty("未读消息数")
    private Integer unreadCount;

    @ApiModelProperty("是否置顶")
    private Boolean isPinned;

    @ApiModelProperty("是否免打扰")
    private Boolean isMuted;

    @ApiModelProperty("对方在线状态（私聊专用）")
    private Boolean isOnline;
}
