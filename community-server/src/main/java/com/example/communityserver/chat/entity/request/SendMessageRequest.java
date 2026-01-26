package com.example.communityserver.chat.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发送消息请求
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("发送消息请求")
public class SendMessageRequest {

    @NotNull(message = "消息类型不能为空")
    @ApiModelProperty(value = "消息类型：1-文本 2-图片 3-文件 4-系统", required = true, example = "1")
    private Integer msgType;

    @NotBlank(message = "消息内容不能为空")
    @ApiModelProperty(value = "消息内容", required = true, example = "你好")
    private String content;

    @ApiModelProperty(value = "引用的消息ID", example = "1001")
    private Long quoteMsgId;

    @ApiModelProperty(value = "扩展数据（JSON字符串）", example = "{\"width\":800,\"height\":600}")
    private String extraData;
}
