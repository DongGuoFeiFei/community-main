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
 * 帖子
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article")
@ApiModel
public class Article {
    @TableId(type = IdType.AUTO)
    private Long articleId;
    @ApiModelProperty(value = "发帖用户ID", example = "1001")
    private Long userId;
    @ApiModelProperty(value = "封面文件id", example = "1001")
    private Long fileId;
    @ApiModelProperty(value = "帖子标题", example = "标题")
    private String title;
    @ApiModelProperty(value = "帖子正文内容", example = "这是一个帖子内容")
    private String content;
    @ApiModelProperty(value = "图片URL（可空，支持多图需另建表）", example = "http://example.com/image.jpg")
    private String imageUrl;
    @ApiModelProperty(value = "视频URL（可空）", example = "http://example.com/video.mp4")
    private String videoUrl;
    @ApiModelProperty(value = "发布位置（如“北京市海淀区”）", example = "北京市海淀区")
    private String location;
    @ApiModelProperty(value = "是否公开（false为仅好友可见）", example = "true")
    private Integer isPublic;
    @ApiModelProperty(value = "发布时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    @ApiModelProperty(value = "最后编辑时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updatedAt;
    @ApiModelProperty(value = "是否删除", example = "false")
    private Integer isDel;
    @ApiModelProperty(value = "是否草稿", example = "false")
    private Integer isDrafts;
}
