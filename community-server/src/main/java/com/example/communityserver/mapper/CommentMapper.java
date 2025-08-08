package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.Comment;
import com.example.communityserver.entity.request.CommentQuery;
import com.example.communityserver.entity.response.CommentListVo;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.ReplyVo;
import org.apache.ibatis.annotations.Param;

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

    List<CommentVo> getCommentsByArticleId(Integer postId);

    ReplyVo getReplyById(Long commentId);

    IPage<CommentListVo> getCommentList(@Param("commentListVoPage") Page<CommentListVo> commentListVoPage,@Param("query") CommentQuery query);
}
