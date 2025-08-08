package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.request.AddCommentDto;
import com.example.communityserver.entity.request.CommentQuery;
import com.example.communityserver.entity.response.CommentListVo;
import com.example.communityserver.entity.response.CommentVo;
import com.example.communityserver.entity.response.ReplyVo;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.utils.web.Result;
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
     *
     * todo 查询时先找redis，若没有找mysql  减少mysql压力 如果查询直接使用redis，但是该文章有新评论插入时，进行一次mysql查询放入redis中
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

    @ApiOperation("获取评论管理列表")
    @GetMapping("/admin")
    public Result<Result.PageData<CommentListVo>> getCommentList(CommentQuery query) {
        IPage<CommentListVo> page = commentService.getCommentList(query);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error("获取失败");
    }


}
