package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.request.AddCommentDto;
import com.example.communityserver.entity.model.Comment;
import com.example.communityserver.entity.request.CommentQuery;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.response.CommentListVo;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.ReplyVo;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/


public interface ICommentService extends IService<Comment> {
    List<CommentVo> getCommentsById(Integer postId);

    ReplyVo addComment(AddCommentDto addCommentDto);

    IPage<NotificationListVo> getCommentNotificationsVo(GetNotificationsParam param);

    IPage<CommentListVo> getCommentList(CommentQuery query);

    boolean deleteComment(Long commentId);

    boolean isOpenCommentById(Integer postId);
}
