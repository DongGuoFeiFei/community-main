package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.dto.AddArticleDto;
import com.example.communityserver.entity.po.Article;
import com.example.communityserver.entity.vo.ArticleCardVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.FileEntityMapper;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private static final String UPLOAD_DIR = "uploads";
    private static final String IMAGE_DIR = "images";

    @Autowired
    private ArticleMapper postsMapper;

    @Autowired
    private FileEntityMapper fileEntityMapper;

    @Override
    public List<ArticleCardVo> getPostsCardVoList(String title) {


        return postsMapper.getPostsCardVoList(title);

    }

    @Override
    public List<ArticleCardVo> getPostsCardVoById(Long id) {
        return postsMapper.getPostsCardVoById(id);
    }

    @Override
    public boolean addArticle(AddArticleDto dto) {
        // 获取文件信息，将信息放入文章中
        Article article = new Article();
        article.setContent(dto.getContent());
        article.setFileId(dto.getFileId());
        article.setTitle(dto.getTitle());
        article.setIsDrafts(0);
        article.setUserId(SecurityUtils.getLoginUserId());
        return postsMapper.insert(article) > 0;
    }

}
