package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.request.AddCommentDto;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Comment;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.ReplyVo;
import com.example.communityserver.mapper.*;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private NotificationEntityMapper notificationEntityMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileEntityMapper fileEntityMapper;

    @Override
    public List<CommentVo> getCommentsById(Integer postId) {
        return commentMapper.getCommentsById(postId);
    }

    @Transactional
    @Override
    public ReplyVo addComment(AddCommentDto addCommentDto) {
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
        NotificationEntity notificationEntity = new NotificationEntity();
        if (comment.getParentId() != null) { // 回复
            Comment parentComment = commentMapper.selectById(comment.getParentId());
            notificationEntity.setType("reply");
            notificationEntity.setUserId(parentComment.getUserId());
            notificationEntity.setParentSourceId(parentComment.getCommentId());
        } else { // 评论
            Article article = articleMapper.selectById(comment.getArticleId());
            notificationEntity.setType("comment");
            notificationEntity.setUserId(article.getUserId());
            notificationEntity.setParentSourceId(article.getArticleId());
        }
        notificationEntity.setSonSourceId(comment.getCommentId());
        notificationEntityMapper.insert(notificationEntity);

        // 获取返回的数据
        return commentMapper.getReplyById(comment.getCommentId());
    }
}
