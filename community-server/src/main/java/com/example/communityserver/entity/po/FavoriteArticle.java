package com.example.communityserver.entity.po;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
@ApiModel
public class FavoriteArticle {
    /**
     * 收藏记录ID
     */
    private Long favoriteId;

    /**
     * 收藏用户ID
     */
    private Long userId;

    /**
     * 被收藏的帖子ID
     */
    private Long articleId;

    /**
     * 收藏时间
     */
    private Date createdAt;

    /**
     * 收藏夹ID（可为空表示默认收藏夹）
     */
    private Long folderId;
}