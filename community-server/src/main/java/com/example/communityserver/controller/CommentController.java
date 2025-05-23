package com.example.communityserver.controller;

import com.example.communityserver.entity.dto.AddCommentDto;
import com.example.communityserver.entity.po.Comment;
import com.example.communityserver.entity.vo.CommentVo;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 评论区功能
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-03
 **/

@Api(tags = "评论区")
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @ApiOperation("获取评论")
    @GetMapping("/getCommentsById")
    public Result<List<CommentVo>> getCommentsById(Integer postId) {
        // TODO: 2025/5/4 后续可以添加评论区分页功能
        List<CommentVo> list = commentService.getCommentsById(postId);
        return Result.success(list);
    }

    @ApiOperation("添加新的评论")
    @PostMapping("/addComment")
    public Result<Comment> addComment(@RequestBody AddCommentDto addCommentDto) {
        Comment comment = commentService.addComment(addCommentDto);
        return comment != null ? Result.success(comment) : Result.error("评论失败，请稍后重试");
    }
}
