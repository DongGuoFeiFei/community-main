package com.example.communityserver.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-07
 **/

@Data
@ApiModel("评论VO")
public class CommentListVo {
    @ApiModelProperty("评论ID")
    private Long commentId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty("文章信息")
    private ArticleInfo article;

    @ApiModelProperty("用户信息")
    private UserInfo user;

    @ApiModelProperty("父评论信息")
    private ParentCommentInfo parentComment;

    @Data
    @ApiModel("文章信息")
    public static class ArticleInfo {
        @ApiModelProperty("文章ID")
        private Long articleId;

        @ApiModelProperty("文章标题")
        private String title;
    }

    @Data
    @ApiModel("用户信息")
    public static class UserInfo {
        @ApiModelProperty("用户ID")
        private Long userId;

        @ApiModelProperty("用户名")
        private String username;

        @ApiModelProperty("昵称")
        private String nickname;

        @ApiModelProperty("头像")
        private String avatar;
    }

    @Data
    @ApiModel("父评论信息")
    public static class ParentCommentInfo {
        @ApiModelProperty("评论ID")
        private Long commentId;

        @ApiModelProperty("评论内容")
        private String content;

        @ApiModelProperty("用户信息")
        private UserInfo user;
    }
}
