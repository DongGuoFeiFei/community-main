package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章浏览记录 实体类
 */
@Data
@TableName("article_view")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleView {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long articleViewId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 浏览用户ID（null表示未登录用户）
     */
    private Long userId;

    /**
     * 浏览者IP地址
     */
    private String ipAddress;

    /**
     * 浏览时间,更新时间（重复观看，时间更新）
     */
    private String viewTime;

    /**
     * 设备信息（如：Mozilla/5.0 (Windows NT 10.0...)）
     */
    private String deviceInfo;

//    private Long notificationId;
}