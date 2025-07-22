package com.example.communityserver.entity.response;

import com.example.communityserver.entity.model.ContentCategory;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-22
 **/

@Data
public class ContentCategoryTree {
    private Long id;
    private Long parentId;
    private String categoryName;
    private String categorySlug;
    private String description;
    private String coverUrl;
    private Integer sortOrder;
    private Integer status;
    private Integer articleCount;
    private Date createdAt;
    private Date updatedAt;
    private Integer isDel;
    private List<ContentCategory> categoryList;
}
