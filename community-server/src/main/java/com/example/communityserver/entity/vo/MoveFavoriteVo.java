package com.example.communityserver.entity.vo;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Data
public class MoveFavoriteVo {
    private Long id;
    private Long articleId;
    private Date createdAt;
    private Long folderId;
}