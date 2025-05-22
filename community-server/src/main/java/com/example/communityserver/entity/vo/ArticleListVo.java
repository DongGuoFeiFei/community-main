package com.example.communityserver.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-21
 **/

@Data
public class ArticleListVo {
    private Long id;                    // 文章ID
    private String title;               // 文章标题
    private String content;             // 文章内容
    private String coverUrl = "";       // 封面URL（默认空字符串）
    private Integer status;              // 状态：0是 draft/ 1是 published
    private Integer viewCount = 0;      // 浏览量（默认0）
    private Integer likeCount = 0;      // 点赞数（默认0）
    private Integer commentCount = 0;   // 评论数（默认0）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;   // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;   // 更新时间
}