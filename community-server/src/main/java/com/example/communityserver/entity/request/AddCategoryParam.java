package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-18
 **/

@Data
public class AddCategoryParam {
    private Long id;
    private String categoryName;
    private String categorySlug;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private String description;
    private String coverUrl;
}
