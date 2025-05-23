package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.po.Comment;
import com.example.communityserver.entity.vo.CommentVo;
import com.example.communityserver.entity.vo.ReplyVo;

import java.util.List;

/**
 * <p>
 * 评论
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/


public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> getCommentsById(Integer postId);

    ReplyVo getReplyById(Long commentId);
}
