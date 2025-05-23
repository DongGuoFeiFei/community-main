package com.example.communityserver.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/

@Data
public class ReplyVo {
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String nickname; //网名
    private String accessUrl; //头像
    private String content; //评论内容
    private Long parentId; //父ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    private String repliedNickname;
}
