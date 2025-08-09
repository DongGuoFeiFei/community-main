package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.response.ContentCategoryTree;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/


public interface ContentCategoryMapper extends BaseMapper<ContentCategory> {
    List<ContentCategoryTree> getCategoryTrees();
}
