package com.example.communityserver.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户收藏记录实体类
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorites")
@ApiModel(value = "用户收藏记录实体类")
public class Favorite {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "收藏记录ID", example = "1")
    private Long favoriteId;

    @ApiModelProperty(value = "收藏用户ID", example = "123")
    private Long userId;

    @ApiModelProperty(value = "被收藏的帖子ID", example = "456")
    private Long articleId;

    @ApiModelProperty(value = "收藏时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    @ApiModelProperty(value = "收藏夹ID", example = "789", notes = "为空表示默认收藏夹")
    private Long folderId;
}