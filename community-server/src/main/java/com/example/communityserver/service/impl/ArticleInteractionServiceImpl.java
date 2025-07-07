package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ArticleInteraction;
import com.example.communityserver.mapper.ArticleInteractionMapper;
import com.example.communityserver.service.IArticleInteractionService;
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
public class ArticleInteractionServiceImpl extends ServiceImpl<ArticleInteractionMapper, ArticleInteraction> implements IArticleInteractionService {
    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;
}
