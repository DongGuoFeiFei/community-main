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

import java.util.Date;

/**
 * 文章实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("article")
@ApiModel("文章")
public class Article {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "文章ID", example = "1001")
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

    @ApiModelProperty(value = "发布位置", example = "北京市海淀区")
    private String location;

    @ApiModelProperty(value = "是否公开(1公开 0不公开)", example = "1")
    private Integer isPublic;

    @ApiModelProperty(value = "是否删除(0删除 1存在)", example = "1")
    private Integer isDel;

    @ApiModelProperty(value = "是否草稿(0发布 1草稿)", example = "0")
    private Integer isDrafts;

    @ApiModelProperty(value = "发布时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty(value = "最后编辑时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    // 新增的热度相关字段
    @ApiModelProperty(value = "浏览数", example = "100")
    private Integer viewCount = 0;

    @ApiModelProperty(value = "点赞数", example = "20")
    private Integer likeCount = 0;

    @ApiModelProperty(value = "评论数", example = "15")
    private Integer commentCount = 0;

    @ApiModelProperty(value = "分享数", example = "5")
    private Integer shareCount = 0;

    @ApiModelProperty(value = "热度分数", example = "85.5")
    private Double hotScore = 0.0;
}