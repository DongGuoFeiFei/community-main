package com.example.communityserver.entity.response;

import com.example.communityserver.entity.model.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-16
 **/

@Data
public class UserPostVo {
    private Long id;
    private String title;
    private Date createdAt;
    private Long viewCount;
    private Long likeCount;
    private String content;
    private String coverUrl;
    private List<Tag> tags;
}
