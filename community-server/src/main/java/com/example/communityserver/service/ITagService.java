package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.CreateTagParam;
import com.example.communityserver.entity.response.TagVo;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/


public interface ITagService extends IService<Tag> {
    List<TagVo> getPostTags(Long postId);

    List<TagVo> getPopularTags();

    int batchInsert(List<Long> tagIds, Long articleId);

    boolean delTagArticle(List<Long> tagIds, Long articleId);

    TagVo createTag(CreateTagParam param);
}
