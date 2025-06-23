package com.example.communityserver.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-22
 **/

@Data
public class UserFavoListVo {
    private Long id;
    private String title;
    private String coverUrl;
    private String author;
    private Long authorId;
    private Integer viewCount;
    private Integer likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String collectTime;
}
