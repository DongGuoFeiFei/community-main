package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Comment;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.AddCommentDto;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.ReplyVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.CommentMapper;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<CommentVo> getCommentsById(Integer postId) {
        List<CommentVo> voList = null;
        voList = redisUtil.getCacheList(CacheKeyConstants.ARTICLE_COMMENT + postId);
        if (voList.isEmpty()) {
            voList = commentMapper.getCommentsByArticleId(postId);
            if (!voList.isEmpty()) {
                redisUtil.setCacheList(CacheKeyConstants.ARTICLE_COMMENT + postId, voList);
                redisUtil.expire(CacheKeyConstants.ARTICLE_COMMENT + postId, 3, TimeUnit.DAYS);
            }
            return voList;
        }
        redisUtil.expire(CacheKeyConstants.ARTICLE_COMMENT + postId, 3, TimeUnit.DAYS);
        return voList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReplyVo addComment(AddCommentDto addCommentDto) {

        // 删除旧数据缓存
        redisUtil.deleteObject(CacheKeyConstants.ARTICLE_COMMENT + addCommentDto.getArticleId());

        // 添加用户通知消息
        // 获取通知者id
        // 添加用户评论
        Long loginUserId = SecurityUtils.getLoginUserId();
        // 添加用户评论
        // TODO: 2025/5/5 研究使用MapStruct处理嵌套数据 
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentDto, comment);
        comment.setUserId(loginUserId);
        commentMapper.insert(comment);

        // parentId是否存在（评论or回复）  添加通知
        Notification notification = new Notification();
        if (comment.getParentId() != null) { // 回复
            Comment parentComment = commentMapper.selectById(comment.getParentId());
            notification.setType(ActiveTypeEnum.REPLY);
            notification.setUserId(parentComment.getUserId());
            notification.setContentId(comment.getCommentId());
        } else { // 评论
            Article article = articleMapper.selectById(comment.getArticleId());
            notification.setType(ActiveTypeEnum.COMMENT);
            notification.setUserId(article.getUserId());
            notification.setContentId(article.getArticleId());
        }
        notification.setSenderId(SecurityUtils.getLoginUserId());
        notificationMapper.insert(notification);

        // 获取返回的数据
        return commentMapper.getReplyById(comment.getCommentId());
    }

    @Override
    public IPage<NotificationListVo> getCommentNotificationsVo(GetNotificationsParam param) {
        IPage<Notification> notificationPage = new Page<>(param.getPage(), param.getSize());

        log.info("{}", param);
        LambdaQueryWrapper<Notification> notificationQueryWrapper = new LambdaQueryWrapper<>();
        notificationQueryWrapper
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getIsRead, Boolean.TRUE.equals(param.getIsRead()) ? 1 : 0)
                .eq(Notification::getType, param.getType())
                .eq(Notification::getIsDel, 0)
                .orderByAsc(Notification::getIsRead)
                .orderByDesc(Notification::getCreatedAt);
        IPage<Notification> notificationIPage = notificationMapper.selectPage(notificationPage, notificationQueryWrapper);

        List<Long> sendIds = notificationIPage.getRecords().stream().map(Notification::getSenderId).distinct().collect(Collectors.toList());
        List<User> senders = sendIds.isEmpty() ? Collections.emptyList() : userMapper.selectBatchIds(sendIds);

        List<Long> contentIds = notificationIPage.getRecords().stream().map(Notification::getContentId).distinct().collect(Collectors.toList());
        List<Comment> comments = contentIds.isEmpty() ? Collections.emptyList() : commentMapper.selectBatchIds(contentIds);
        List<Long> articleIds = comments.stream().map(Comment::getArticleId).distinct().collect(Collectors.toList());
        List<Article> articles = articleIds.isEmpty() ? Collections.emptyList() : articleMapper.selectBatchIds(articleIds);

        // 发起者用户senders  文章名称articles  评论内容comments  通知数据
        //转换为VO列表
        List<NotificationListVo> listVos = notificationIPage
                .getRecords()
                .stream()
                .map(record -> {
                    NotificationListVo vo = new NotificationListVo();
                    vo.setNotificationId(record.getNotificationId());
                    vo.setType(record.getType());
                    vo.setIsRead(record.getIsRead() == 1);
                    // 设置发送者信息
                    senders.stream()
                            .filter(sender -> sender.getUserId().equals(record.getSenderId()))
                            .findFirst()
                            .ifPresent(sender -> {
                                vo.setSenderId(sender.getUserId());
                                vo.setSenderName(sender.getNickname());
                                vo.setSenderAvatar(sender.getAvatar());
                            });
                    // 设置文章信息
                    comments
                            .stream()
                            .filter(comment -> comment.getCommentId().equals(record.getContentId()))
                            .findFirst()
                            .ifPresent(comment -> {
                                articles.stream()
                                        .filter(article -> article.getArticleId().equals(comment.getArticleId()))
                                        .findFirst()
                                        .ifPresent(article -> {
                                            vo.setSourceId(article.getArticleId());
                                            vo.setSourceTitle(article.getTitle());
                                            vo.setRelatedId(comment.getCommentId());
                                            vo.setRelatedContent(comment.getContent());
                                        });
                            });
                    vo.setCreatedAt(record.getCreatedAt());
                    vo.setExtraData(record.getExtraData());
                    return vo;
                })
                .collect(Collectors.toList());
        // 构建返回分页对象
        IPage<NotificationListVo> page = new Page<>();
        page.setRecords(listVos);
        page.setTotal(notificationIPage.getTotal());
        page.setCurrent(notificationIPage.getCurrent());
        page.setSize(notificationIPage.getSize());
        return page;
    }
}
