package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.CreateTagParam;
import com.example.communityserver.entity.response.TagVo;
import com.example.communityserver.mapper.TagMapper;
import com.example.communityserver.mapping.TagMapping;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.core.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        queryWrapper
                .ne(Tag::getStatus, 2)
                .orderByDesc(Tag::getCreateCount);
        List<Tag> tags = tagMapper.selectList(queryWrapper);
        List<Tag> tagList = tags.stream().limit(5).collect(Collectors.toList());
        ArrayList<TagVo> tagVos = new ArrayList<>();
        TagMapping.INSTANCE.toListTagVo(tagList, tagVos);
        return tagVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Long> tagIds, Long articleId) {

        if (tagIds == null || tagIds.isEmpty() || articleId == null) {
            return 0;
        }
        // 次数加一
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Tag::getId, tagIds)
                .setSql("create_count = create_count + 1");
        tagMapper.update(updateWrapper);
        return tagMapper.batchInsert(tagIds, articleId);
    }

    @Override
    public boolean delTagArticle(List<Long> tagIds, Long articleId) {
        if (tagIds == null || tagIds.isEmpty()) {
            return false;
        }
        System.out.println(tagIds);
        return tagMapper.delTagArticle(tagIds, articleId);
    }

    @Override
    public TagVo createTag(CreateTagParam param) {
        Tag tag = new Tag();
        tag.setCreatorId(SecurityUtils.getLoginUserId());
        tag.setName(param.getName());
        tag.setColor(param.getColor());
        if (StringUtil.isBlank(param.getSlug())) {
            param.setSlug(param.getName());
        }
        tag.setSlug(param.getSlug());
        tag.setCreateCount(1L);
        // 检测是否存在此tag，此tag是都被禁止创建,通过名称判断
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, param.getName());
        Tag tag1 = tagMapper.selectOne(queryWrapper);
        if (tag1 != null && tag1.getStatus() == 2) {
            return null;
        }
        int b = tagMapper.insert(tag);
        TagVo tagVo = new TagVo();
        TagMapping.INSTANCE.toTagVo(tag, tagVo);
        return tagVo;
    }
}
