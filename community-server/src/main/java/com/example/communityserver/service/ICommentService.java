package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.dto.AddCommentDto;
import com.example.communityserver.entity.po.Comment;
import com.example.communityserver.entity.vo.CommentVo;
import com.example.communityserver.entity.vo.ReplyVo;

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
}
