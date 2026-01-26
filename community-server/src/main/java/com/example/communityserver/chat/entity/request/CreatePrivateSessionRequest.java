package com.example.communityserver.chat.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建私聊会话请求
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("创建私聊会话请求")
public class CreatePrivateSessionRequest {

    @NotNull(message = "对方用户ID不能为空")
    @ApiModelProperty(value = "对方用户ID", required = true, example = "1001")
    private Long userId;
}
