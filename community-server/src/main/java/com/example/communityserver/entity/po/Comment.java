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
 * 评论
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comments")
@ApiModel
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "关联的帖子ID", example = "1001")
    private Long articleId;

    @ApiModelProperty(value = "评论用户ID", example = "1001")
    private Long userId;

    @ApiModelProperty(value = "评论内容", example = "这是一个评论内容")
    private String content;

    @ApiModelProperty(value = "父评论ID（用于回复功能，NULL表示一级评论）", example = "1002")
    private Long parentId;
    @ApiModelProperty(value = "第一层评论的ID（用于回复功能，NULL表示一级评论）", example = "1002")
    private Long firstId;
    @ApiModelProperty(value = "是否删除", example = "1")
    private Integer isDel;
    @ApiModelProperty(value = "评论时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
}
