package com.example.communityserver.chat.entity.response;

import com.example.communityserver.chat.entity.model.ChatMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("消息分页响应")
public class MessagePageResponse {

    @ApiModelProperty("消息列表")
    private List<ChatMessage> records;

    @ApiModelProperty("是否还有更多消息")
    private boolean hasMore;

    @ApiModelProperty("下一次查询的序列号")
    private Long nextSeq;

    @ApiModelProperty("游标（可与 nextSeq 共用）")
    private Long cursor;

    @ApiModelProperty("上一页的序列号")
    private Long prevSeq;
}


