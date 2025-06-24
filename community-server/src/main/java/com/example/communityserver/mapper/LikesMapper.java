package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Likes;
import com.example.communityserver.entity.response.ArticleDtlVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/


public interface LikesMapper extends BaseMapper<Likes> {

    ArticleDtlVo getArticleLike(@Param("articleId") Long articleId, @Param("userId") Long userId);

}
