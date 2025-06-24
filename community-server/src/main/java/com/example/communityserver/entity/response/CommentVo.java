package com.example.communityserver.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-04
 **/

@Data
public class CommentVo {
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String nickname; //网名
    private String accessUrl; //头像
    private String content; //评论内容
    private Long parentId; //父ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    private Long firstId; // 第一层评论的id
    private String repliedNickname;
    private List<CommentVo> voList;
}
