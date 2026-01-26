package com.example.communityserver.chat.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 标记已读请求
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("标记已读请求")
public class MarkReadRequest {

    @NotNull(message = "消息ID不能为空")
    @ApiModelProperty(value = "已读到的消息ID", required = true, example = "1001")
    private Long messageId;
}
