package com.example.communityserver.chat.entity.response;

import com.example.communityserver.chat.entity.model.ChatMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息分页响应
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("消息分页响应")
public class MessagePageResponse {

    @ApiModelProperty("消息列表")
    private List<ChatMessage> messages;

    @ApiModelProperty("是否还有更多")
    private Boolean hasMore;

    @ApiModelProperty("下一页的游标（最后一条消息的ID）")
    private Long nextCursor;
}
