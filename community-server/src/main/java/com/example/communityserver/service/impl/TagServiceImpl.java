package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.response.TagVo;
import com.example.communityserver.mapper.TagMapper;
import com.example.communityserver.mapping.TagMapping;
import com.example.communityserver.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> getPostTags(Long postId) {
        List<Tag> postTags = tagMapper.getPostTags(postId);
        ArrayList<TagVo> tagVos = new ArrayList<>();
        TagMapping.INSTANCE.toListTagVo(postTags, tagVos);
        return tagVos;
    }

    @Override
    public List<TagVo> getPopularTags() {
        // 后续添加用户喜好
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Tag::getCreateCount);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        List<Tag> tagList = tags.stream().limit(5).collect(Collectors.toList());
        ArrayList<TagVo> tagVos = new ArrayList<>();
        TagMapping.INSTANCE.toListTagVo(tagList, tagVos);
        return tagVos;
    }

    @Override
    public int batchInsert(List<Long> tagIds, Long articleId) {

        if (tagIds == null || tagIds.isEmpty() || articleId == null) {
            return 0;
        }
        return tagMapper.batchInsert(tagIds,articleId);
    }

    @Override
    public boolean delTagArticle(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty() ) {
            return false;
        }
        return tagMapper.delTagArticle(tagIds);
    }
}
