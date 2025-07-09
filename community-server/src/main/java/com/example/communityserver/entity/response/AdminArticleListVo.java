package com.example.communityserver.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-09
 **/

@Data
public class AdminArticleListVo {
    private Long articleId;
    private Long userId;
    private Long fileId;
    private String title;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private String location;
    private Integer isPublic;
    private Integer isDel;
    private Integer isDrafts;
    private Date createdAt;
    private Date updatedAt;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private Integer shareCount = 0;
    private Double hotScore = 0.0;
}
