package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ArticleInteractionTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.ArticleInteraction;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.request.ArticleSearchParam;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.request.SearchParam;
import com.example.communityserver.entity.response.*;
import com.example.communityserver.mapper.ArticleInteractionMapper;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.FileEntityMapper;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.mapping.ArticleMapping;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.markdown.MarkDownUtils;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
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
    private ArticleInteractionMapper articleInteractionMapper;

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private ITagService tagService;


    @Override
    public List<ArticleCardVo> getPostsCardVoById(Long id) {
        // 添加一条阅读记录
        Long loginUserId = SecurityUtils.getLoginUserId();
        // 判断是否已经有了记录，若已有记录则更新阅读时间，若没有记录，则添加一条记录
        LambdaQueryWrapper<ArticleInteraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleInteraction::getArticleId, id)
                .eq(ArticleInteraction::getUserId, loginUserId)
                .eq(ArticleInteraction::getActionType, ArticleInteractionTypeEnum.VIEW)
        ;
        ArticleInteraction articleInteraction = articleInteractionMapper.selectOne(queryWrapper);
        if (articleInteraction == null) {
            // 处理无记录的情况（如初始化浏览记录）
            articleInteraction = new ArticleInteraction();
            articleInteraction.setArticleId(id);
            articleInteraction.setUserId(loginUserId);
            articleInteraction.setActionType(ArticleInteractionTypeEnum.VIEW);
            articleInteractionMapper.insert(articleInteraction);
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.setSql("view_count = view_count + 1");
            articleMapper.update(null, updateWrapper);
        } else {
            // 处理已有记录的情况，更新阅读记录
            articleInteraction.setCreatedAt(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()));
            articleInteractionMapper.updateById(articleInteraction);
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
        int insert = articleMapper.insert(article);
        int batchInsert = tagService.batchInsert(dto.getTagIds(), article.getArticleId());
        return batchInsert > 0;
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
        redisUtil.setCacheObject(CacheKeyConstants.USER_ARTICLE_COUNT + id, articleCount, 3, TimeUnit.DAYS);
        return articleCount;
    }


    @Override
    public IPage<AdminArticleListVo> getAdminArticleList(ArticleSearchParam param) {
        Page<Article> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        queryWrapper
                .eq(StringUtil.isNotBlank(param.getTitle()), Article::getTitle, param.getTitle())
                .ge(StringUtil.isNotBlank(param.getStartTime()), Article::getCreatedAt, param.getStartTime())
                .le(StringUtil.isNotBlank(param.getEndTime()), Article::getCreatedAt, param.getEndTime())
                .eq(Article::getIsDel, 1)
                .orderByDesc(Article::getHotScore);
        IPage<Article> result = articleMapper.selectPage(page, queryWrapper);
        IPage<AdminArticleListVo> voIPage = new Page<>();
        BeanUtils.copyProperties(result, voIPage);
        return voIPage;
    }

}
