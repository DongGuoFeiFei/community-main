package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.request.AddCategoryParam;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.entity.response.ContentCategoryTree;

import java.util.List;

/**
 * <p>
 * 内容分类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/


public interface IContentCategoryService extends IService<ContentCategory> {
    List<ContentCategoryTree> getCategoryTrees();

    IPage<ContentCategory> getCategories(SearchNameStatusParam param);

    Boolean deleteCategory(Long categoryId);

    Boolean addCategory(AddCategoryParam param);

    Boolean updateCategory(AddCategoryParam param);
}
