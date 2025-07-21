package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/


// ContentCategory.java
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("content_category")
public class ContentCategory {
    @TableId(value = "category_id", type = IdType.AUTO)
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
    @TableLogic(value = "0", delval = "1")
    private Integer isDel;
}
