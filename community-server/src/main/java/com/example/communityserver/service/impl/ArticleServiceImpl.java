package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.enums.ArticleInteractionTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.ArticleInteraction;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.request.ArticleSearchParam;
import com.example.communityserver.entity.request.FetchPostsParam;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.response.*;
import com.example.communityserver.mapper.*;
import com.example.communityserver.mapping.ArticleMapping;
import com.example.communityserver.service.IArticleCategoryRelationService;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.markdown.MarkDownUtils;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/

@Service
@Slf4j
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

    @Autowired
    private IArticleCategoryRelationService articleCategoryRelationService;

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;


    @Override
    public List<ArticleCardVo> getPostsCardVoById(Long id) {
        // 添加一条阅读记录
        Long loginUserId = SecurityUtils.getLoginUserId();
        // 判断是否已经有了记录，若已有记录则更新阅读时间，若没有记录，则添加一条记录
        LambdaQueryWrapper<ArticleInteraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleInteraction::getArticleId, id).eq(ArticleInteraction::getUserId, loginUserId).eq(ArticleInteraction::getActionType, ArticleInteractionTypeEnum.VIEW);
        ArticleInteraction articleInteraction = articleInteractionMapper.selectOne(queryWrapper);
        if (articleInteraction == null) {
            // 处理无记录的情况（如初始化浏览记录）
            articleInteraction = new ArticleInteraction();
            articleInteraction.setArticleId(id);
            articleInteraction.setUserId(loginUserId);
            articleInteraction.setActionType(ArticleInteractionTypeEnum.VIEW);
            articleInteractionMapper.insert(articleInteraction);
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getArticleId, id).setSql("view_count = view_count + 1");
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
        FileEntity fileEntity = fileEntityMapper.selectById(dto.getFileId());
        article.setCoverUrl(fileEntity.getAccessUrl());
        article.setTitle(dto.getTitle());
        article.setIsDrafts(dto.getStatus());
        article.setUserId(SecurityUtils.getLoginUserId());
        int insert = articleMapper.insert(article);
        redisUtil.deleteObject(CacheKeyConstants.USER_ARTICLE_COUNT + SecurityUtils.getLoginUserId());
        tagService.batchInsert(dto.getTagIds(), article.getArticleId());
        int batchInsert = articleCategoryRelationService.batchInsert(dto.getCategoryIds(), article.getArticleId());
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
        // TODO: 2025/7/16 like的数据获取优化
        ArticleDtlVo articleDtlVo2 = likesMapper.getArticleLike(id, SecurityUtils.getLoginUserId());
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo1, articleDtlVo);
        ArticleMapping.INSTANCE.updateArticle(articleDtlVo2, articleDtlVo);
        if (articleDtlVo1 == null) {
            return null;
        }
        // TODO: 2025/7/17 目前未将未登录用户计算在浏览记录内，考虑IP
        LambdaQueryWrapper<ArticleInteraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleInteraction::getActionType, ArticleInteractionTypeEnum.VIEW).eq(ArticleInteraction::getArticleId, articleDtlVo.getArticleId()).eq(SecurityUtils.getLoginUserId() != null, ArticleInteraction::getUserId, SecurityUtils.getLoginUserId());
        if (articleInteractionMapper.selectOne(queryWrapper) == null && SecurityUtils.getLoginUserId() != null) {
            ArticleInteraction articleInteraction = new ArticleInteraction(articleDtlVo.getArticleId(), SecurityUtils.getLoginUserId(), ArticleInteractionTypeEnum.VIEW, null);
            articleInteractionMapper.insert(articleInteraction);
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getArticleId, articleDtlVo.getArticleId()).setSql("view_count = view_count + 1");
            articleMapper.update(null, updateWrapper);
        }
        return articleDtlVo;
    }

    @Override
    public Page<ArticleCardVo> getPostsCardVoList(FetchPostsParam param) {
        // 首页搜索内容
        Page<ArticleCardVo> page = new Page<>(param.getPageNum(), param.getPageSize());
        List<Long> categoryIds = new ArrayList<>();
        if (StringUtil.isBlank(param.getTitle()) && param.getCategoryId() != null) {
            //没有名称搜索且不是null，此时判断词条id下面是否有着子集
            ContentCategory contentCategory = contentCategoryMapper.selectById(param.getCategoryId());
            LambdaQueryWrapper<ContentCategory> queryWrapper = new LambdaQueryWrapper<>();
            List<ContentCategory> categories = new ArrayList<>();
            if (contentCategory.getParentId() == null) {
                // 添加子集
                queryWrapper.eq(ContentCategory::getParentId, param.getCategoryId());
                categories = contentCategoryMapper.selectList(queryWrapper);
            } else {
                // 添加父级
                queryWrapper.eq(ContentCategory::getId, contentCategory.getParentId());
                categories = contentCategoryMapper.selectList(queryWrapper);
            }
            categoryIds = categories.stream().map(ContentCategory::getId).collect(Collectors.toList());
            categoryIds.add(param.getCategoryId());
        }
        log.info("{}", categoryIds);
        Page<ArticleCardVo> voPage = articleMapper.getPostsCardVoList(page, param, categoryIds);
        voPage.getRecords().forEach(articleCardVo -> {
            String html = MarkDownUtils.toHtml(articleCardVo.getContent());
            articleCardVo.setContent(StringUtil.truncate(StringUtil.htmlToPlainText(html), 100));
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
        queryWrapper.like(StringUtil.isNotBlank(param.getTitle()), Article::getTitle, param.getTitle()).ge(StringUtil.isNotBlank(param.getStartTime()), Article::getCreatedAt, param.getStartTime()).le(StringUtil.isNotBlank(param.getEndTime()), Article::getCreatedAt, param.getEndTime()).eq(Article::getIsDel, 1).orderByDesc(Article::getHotScore);
        IPage<Article> result = articleMapper.selectPage(page, queryWrapper);
        IPage<AdminArticleListVo> voIPage = new Page<>();
        BeanUtils.copyProperties(result, voIPage);
        return voIPage;
    }

    @Override
    public List<UserPostVo> getUserPosts(Long userId) {
        List<UserPostVo> userPosts = articleMapper.getUserPosts(userId);
        userPosts.forEach(userPostVo -> {
            userPostVo.setCoverUrl(SystemConstants.BASIC_URL + userPostVo.getCoverUrl());
        });
        return userPosts;
    }

    @Override
    public List<UserPostVo> getUserFavorites(Long userId) {
        List<UserPostVo> userPosts = articleMapper.getUserFavorites(userId, ActiveTypeEnum.FAVORITE_ARTICLE);
        userPosts.forEach(userPostVo -> {
            userPostVo.setCoverUrl(SystemConstants.BASIC_URL + userPostVo.getCoverUrl());
        });
        return userPosts;

    }

}
