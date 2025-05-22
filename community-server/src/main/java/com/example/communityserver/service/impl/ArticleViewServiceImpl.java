package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.ArticleView;
import com.example.communityserver.mapper.ArticleViewMapper;
import com.example.communityserver.service.IArticleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-21
 **/

@Service
public class ArticleViewServiceImpl extends ServiceImpl<ArticleViewMapper, ArticleView> implements IArticleViewService {
    @Autowired
    private ArticleViewMapper articleViewMapper;
}
