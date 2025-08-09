package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/


public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getPostTags(Long postId);

    int batchInsert(@Param("tagIds") List<Long> tagIds,@Param("articleId") Long articleId);

    boolean delTagArticle(@Param("tagIds") List<Long> tagIds,@Param("articleId") Long articleId);
}
