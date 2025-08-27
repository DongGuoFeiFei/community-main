package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/


@Data
@NoArgsConstructor
@AllArgsConstructor
// todo 注意：@TableName 需要动态处理，这里先写一个基础表名，聊天室表格，实体类和mysql表格，分表+分区+归档，仅保存近期表格
@TableName("im_message")
@ApiModel(value = "消息实体类")
public class ImMessage {

    @TableId(type = IdType.ASSIGN_ID) // 使用雪花算法，避免分表ID冲突
    @ApiModelProperty(value = "消息ID", example = "1663123456789123456")
    private Long id;

    @ApiModelProperty(value = "会话ID", example = "1")
    private Long sessionId;

    @ApiModelProperty(value = "发送者用户ID", example = "456")
    private Long senderId;

    @ApiModelProperty(value = "发送者昵称", example = "张三")
    private String senderName;

    @ApiModelProperty(value = "发送者头像URL")
    private String senderAvatar;

    @ApiModelProperty(value = "消息类型：1-文本，2-图片，3-文件，4-语音，5-视频，6-系统通知", example = "1")
    private Integer contentType;

    @ApiModelProperty(value = "消息内容", example = "大家好！")
    private String content;

    @ApiModelProperty(value = "文件大小（字节）", example = "102400")
    private Long fileSize;

    @ApiModelProperty(value = "逻辑删除标志", example = "0")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "客户端发送时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
