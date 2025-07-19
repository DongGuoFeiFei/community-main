package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Comment;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.request.AddCommentDto;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.ReplyVo;
import com.example.communityserver.mapper.*;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/

@Service
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
    private FileEntityMapper fileEntityMapper;
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
}
