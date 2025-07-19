package com.example.communityserver.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/

@Data
public class ArticleCardVo {
    private Long articleId;
    private String title;
    private String nickname;
    @ApiModelProperty(value = "帖子正文内容", example = "这是一个帖子内容")
    private String content;
    @ApiModelProperty(value = "图片URL（可空，支持多图需另建表）", example = "http://example.com/image.jpg")
    private String coverUrl;
    @ApiModelProperty(value = "发布时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    @ApiModelProperty(value = "最后编辑时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updatedAt;
}
