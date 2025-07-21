package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("article_category_relation")
public class ArticleCategoryRelation {
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Long relationId;

    private Long articleId;
    private Long categoryId;
    private Date createdAt;
}