package com.example.communityserver.chat.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 撤回消息请求
 *
 * @author DongGuo
 * @since 2026-01-25
 */
@Data
@ApiModel("撤回消息请求")
public class RecallMessageRequest {

    @ApiModelProperty(value = "消息ID", required = true)
    @NotNull(message = "消息ID不能为空")
    private Long messageId;
}
