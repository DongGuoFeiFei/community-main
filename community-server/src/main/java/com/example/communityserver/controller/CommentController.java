package com.example.communityserver.controller;

import com.example.communityserver.entity.dto.AddCommentDto;
import com.example.communityserver.entity.vo.CommentVo;
import com.example.communityserver.entity.vo.ReplyVo;
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


    /*
     * 两种获取评论区的数据方式
     * 一、获取评论区多层级树图
     * 二、获取第一层之后在获取第一层之下所有的评论数据
     *
     * todo 后续实现分页效果、评论删除效果 按照评论的时间顺序进行排序
     * */
    @ApiOperation("获取评论")
    @GetMapping("/getCommentsById")
    public Result<List<CommentVo>> getCommentsById(Integer postId) {
        List<CommentVo> list = commentService.getCommentsById(postId);
        return Result.success(list);
    }

    @ApiOperation("添加新的评论")
    @PostMapping("/addComment")
    public Result<ReplyVo> addComment(@RequestBody AddCommentDto addCommentDto) {
        ReplyVo replyVo = commentService.addComment(addCommentDto);
        return replyVo != null ? Result.success(replyVo) : Result.error("评论失败，请稍后重试");
    }
}
