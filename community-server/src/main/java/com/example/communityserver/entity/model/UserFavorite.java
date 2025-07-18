package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 用户收藏文章实体类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_favorites")
@ApiModel(value = "用户收藏实体类")
public class UserFavorite {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "收藏ID", example = "1")
    private Long favoriteId;
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;
    @ApiModelProperty(value = "喜欢目标的ID", example = "1")
    private Long targetId;
    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty(value = "收藏夹ID", example = "1")
    private Long folderId;
    @ApiModelProperty(value = "收藏类型", example = "ARTICLE")
    private ActiveTypeEnum type;

//    private Long notificationId;
}