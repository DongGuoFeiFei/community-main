package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.ArticleCategoryRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/


public interface ArticleCategoryRelationMapper extends BaseMapper<ArticleCategoryRelation> {
    int batchInsert(@Param("categoryIds") List<Long> categoryIds,@Param("articleId") Long articleId);

    boolean delACRelation(@Param("categoryIds") List<Long> categoryIds,@Param("articleId") Long articleId);
}
