package com.example.communityserver.chat.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("标记消息已读请求")
public class MessageReadRequest {

    @NotNull(message = "会话ID不能为空")
    @ApiModelProperty("会话ID")
    private Long sessionId;

    @NotNull(message = "已读序列号不能为空")
    @ApiModelProperty("已读的最大序列号")
    private Long readSeq;
}


