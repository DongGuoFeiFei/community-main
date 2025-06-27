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

/**
 * <p>
 * 用户点赞记录实体类
 * </p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("likes")
@ApiModel(value = "点赞记录实体类")
public class Likes {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "点赞记录ID", example = "1")
    private Long likeId;

    @ApiModelProperty(value = "被点赞的内容ID", example = "123")
    private Long targetId;

    @ApiModelProperty(value = "点赞用户ID", example = "456")
    private Long userId;

    @ApiModelProperty(value = "用户是否喜欢", example = "0不喜欢，1喜欢")
    private Integer isLike;

    @ApiModelProperty(value = "点赞时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    @ApiModelProperty(value = "点赞类型", example = "article", allowableValues = "comment,follow,article")
    private NotificationTypeEnum type;

//    private Long notificationId;
}