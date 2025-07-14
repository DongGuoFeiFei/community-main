package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 系统通知（点赞、评论、关注等）
 * <p>
 *
 * @author: Your Name
 * @create: 2023-10-01
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("notifications")
@ApiModel
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long notificationId;
    @ApiModelProperty(value = "接收通知的用户ID", example = "1001")
    private Long userId;
    @ApiModelProperty(value = "通知类型", example = "like")
    private NotificationTypeEnum type;
    @ApiModelProperty(value = "触发通知的接收ID（被评论者）（如点赞ID、评论ID）", example = "1001")
    private Long parentSourceId;
    @ApiModelProperty(value = "触发通知的发起ID（评论者）（如点赞ID、评论ID）", example = "1001")
    private Long sonSourceId;
    @ApiModelProperty(value = "0已读 1 未读", example = "1")
    private Integer isRead;
    @ApiModelProperty(value = "通知时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    private Integer isDel;
}
