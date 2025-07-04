package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.ArticleView;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.request.SearchParam;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import com.example.communityserver.entity.response.ArticleListVo;
import com.example.communityserver.entity.response.EditorArticlesVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.ArticleViewMapper;
import com.example.communityserver.mapper.FileEntityMapper;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.mapping.ArticleMapping;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.markdown.MarkDownUtils;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private RedisUtil redisUtil;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FileEntityMapper fileEntityMapper;

    @Autowired
    private ArticleViewMapper articleViewMapper;

    @Autowired
    private LikesMapper likesMapper;


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
        return articleMapper.getPostsCardVoById(id);
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
        return articleMapper.insert(article) > 0;
    }


    @Override
    public boolean delById(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getIsDel, 0).eq(Article::getArticleId, id);
        Article article = new Article();
        return articleMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public EditorArticlesVo getEditorArticleDtl(Long id) {
        Long loginUserId = SecurityUtils.getLoginUserId();
        return articleMapper.getEditorArticleDtl(id, loginUserId);

    }

    @Override
    public ArticleDtlVo getArticleDtlVo(Long id) {
        ArticleDtlVo articleDtlVo = new ArticleDtlVo();
        ArticleDtlVo articleDtlVo1 = articleMapper.getArticleDtlVo(id);
        ArticleDtlVo articleDtlVo2 = likesMapper.getArticleLike(id, SecurityUtils.getLoginUserId());
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo1, articleDtlVo);
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo2, articleDtlVo);
        return articleDtlVo;
    }

    @Override
    public Page<ArticleCardVo> getPostsCardVoList(SearchParam param) {

        Page<ArticleCardVo> page = new Page<>(param.getPageNum(), param.getPageSize());
        Page<ArticleCardVo> voPage = articleMapper.getPostsCardVoList(page, param.getTitle());
        voPage.getRecords().forEach(articleCardVo -> {
            String content = MarkDownUtils.toPlainText(articleCardVo.getContent());
            articleCardVo.setContent(StringUtil.truncate(content, 100));
        });
        return voPage;
    }


    @Override
    public Page<ArticleListVo> getArticleList(GetArticleListDto dto) {

        Page<ArticleListVo> page = new Page<>(dto.getPage(), dto.getSize());
        return articleMapper.getArticleList(page, dto, SecurityUtils.getLoginUserId());
    }

    @Override
    public Long countByUser(Long id) {
        Long articleCount = redisUtil.getCacheObject(CacheKeyConstants.USER_ARTICLE_COUNT + id);
        if (articleCount == null) {
            articleCount = articleMapper.countByUser(id);
        }
        redisUtil.expire(CacheKeyConstants.USER_ARTICLE_COUNT + id, 3, TimeUnit.DAYS);
        return articleCount;
    }

}
