package com.example.communityserver.entity.model;

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
 * 用户收藏夹实体类
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("favorite_folders")
@ApiModel(value = "用户收藏夹实体类")
public class FavoriteFolder {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "收藏夹ID", example = "1")
    private Long folderId;

    @ApiModelProperty(value = "所属用户ID", example = "123")
    private Long userId;

    @ApiModelProperty(value = "收藏夹名称", example = "技术文章")
    private String folderName;

    @ApiModelProperty(value = "是否为默认收藏夹", example = "0", notes = "1是，0否")
    private Integer isDefault;

    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
}