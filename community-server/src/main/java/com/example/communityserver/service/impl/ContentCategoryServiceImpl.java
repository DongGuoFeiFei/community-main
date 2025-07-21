package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.mapper.ArticleCategoryRelationMapper;
import com.example.communityserver.mapper.ContentCategoryMapper;
import com.example.communityserver.service.IContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/

@Service
public class ContentCategoryServiceImpl extends ServiceImpl<ContentCategoryMapper, ContentCategory> implements IContentCategoryService {
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Autowired
    private ArticleCategoryRelationMapper articleCategoryRelationMapper;
}
