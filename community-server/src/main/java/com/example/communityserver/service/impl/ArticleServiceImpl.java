package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.dto.AddArticleDto;
import com.example.communityserver.entity.po.Article;
import com.example.communityserver.entity.po.ArticleView;
import com.example.communityserver.entity.vo.ArticleCardVo;
import com.example.communityserver.entity.vo.ArticleDtlVo;
import com.example.communityserver.entity.vo.ArticleListVo;
import com.example.communityserver.entity.vo.EditorArticlesVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.ArticleViewMapper;
import com.example.communityserver.mapper.FileEntityMapper;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.mapping.ArticleMapping;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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


    @Autowired
    private ArticleMapper postsMapper;

    @Autowired
    private FileEntityMapper fileEntityMapper;

    @Autowired
    private ArticleViewMapper articleViewMapper;

    @Autowired
    private LikesMapper likesMapper;

    @Override
    public List<ArticleCardVo> getPostsCardVoList(String title) {


        return postsMapper.getPostsCardVoList(title);

    }

    @Override
    public List<ArticleCardVo> getPostsCardVoById(Long id) {
        // 添加一条阅读记录
        Long loginUserId = SecurityUtils.getLoginUserId();
        // 判断是否已经有了记录，若已有记录则更新阅读时间，若没有记录，则添加一条记录
        LambdaQueryWrapper<ArticleView> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleView::getArticleId, id)
                .eq(ArticleView::getUserId, loginUserId);
        ArticleView articleView = articleViewMapper.selectOne(queryWrapper);
        if (articleView == null) {
            // 处理无记录的情况（如初始化浏览记录）
            articleView = new ArticleView();
            articleView.setArticleId(id);
            articleView.setUserId(loginUserId);
            articleViewMapper.insert(articleView);
        } else {
            // 处理已有记录的情况，更新阅读记录
            articleView.setViewTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()));
            articleViewMapper.updateById(articleView);
        }
        return postsMapper.getPostsCardVoById(id);
    }

    @Override
    public boolean addArticle(AddArticleDto dto) {
        // 获取文件信息，将信息放入文章中
        Article article = new Article();
        article.setContent(dto.getContent());
        article.setFileId(dto.getFileId());
        article.setTitle(dto.getTitle());
        article.setIsDrafts(dto.getStatus());
        article.setUserId(SecurityUtils.getLoginUserId());
        return postsMapper.insert(article) > 0;
    }

    @Override
    public List<ArticleListVo> getArticleList(String title, Integer status, String sortField, Boolean isAsc) {
        return postsMapper.getArticleList(title, status, sortField, isAsc);
    }

    @Override
    public boolean delById(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getIsDel, 0).eq(Article::getArticleId, id);
        Article article = new Article();
        return postsMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public EditorArticlesVo getEditorArticleDtl(Long id) {
        Long loginUserId = SecurityUtils.getLoginUserId();
        return postsMapper.getEditorArticleDtl(id, loginUserId);

    }

    @Override
    public ArticleDtlVo getArticleDtlVo(Long id) {
        ArticleDtlVo articleDtlVo = new ArticleDtlVo();
        ArticleDtlVo articleDtlVo1 = postsMapper.getArticleDtlVo(id);
        ArticleDtlVo articleDtlVo2 = likesMapper.getArticleLike(id, SecurityUtils.getLoginUserId());
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo1, articleDtlVo);
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo2, articleDtlVo);
        return articleDtlVo;
    }

}
