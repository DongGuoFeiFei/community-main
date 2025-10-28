package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.IdStatusParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.entity.request.TagFormParam;
import com.example.communityserver.core.security.aop.RequiresPermission;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/

@Api(tags = "管理员标签管理")
@RestController
@RequestMapping("admin/tags")
public class AdminTagController {
    @Autowired
    private ITagService tagService;

    @ApiOperation("所有标签")
    @RequiresPermission(api = {"admin:tags:get"}, role = {"super_admin"})
    @GetMapping()
    public Result<Result.PageData<Tag>> getTagList(SearchNameStatusParam param) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getIsDel, 0).like(param.getName() != null, Tag::getName, param.getName()).eq(param.getStatus() != null, Tag::getStatus, param.getStatus());
        IPage<Tag> tagPage = new Page<>(param.getPage(), param.getSize());
        IPage<Tag> page = tagService.page(tagPage, queryWrapper);
        return Result.success(new Result.PageData<>(page.getTotal(), page.getRecords()));
    }

    @ApiOperation("批量删除")
    @DeleteMapping("batch")
    @RequiresPermission(api = {"admin:tags:batch:delete"}, role = {"super_admin"})
    public Result<Void> batchDeleteTags(@RequestBody IdsListParam param) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Tag::getId, param.getIds()).set(Tag::getIsDel, 1);
        boolean update = tagService.update(updateWrapper);
        return update ? Result.success() : Result.error();
    }

    @ApiOperation("删除")
    @DeleteMapping("{id}")
    @RequiresPermission(api = {"admin:tags:{id}:delete"}, role = {"super_admin"})
    public Result<Void> deleteTag(@PathVariable Long id) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId, id).set(Tag::getIsDel, 1);
        boolean update = tagService.update(updateWrapper);
        return update ? Result.success() : Result.error();
    }

    @ApiOperation("审批")
    @RequiresPermission(api = {"admin:tags:put"}, role = {"super_admin"})
    @PutMapping()
    public Result<Void> approvalTag(@RequestBody IdStatusParam param) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId, param.getId()).set(Tag::getStatus, param.getStatus());
        boolean update = tagService.update(updateWrapper);
        return update ? Result.success() : Result.error();
    }

    @PostMapping()
    @ApiOperation("创建标签")
    @RequiresPermission(api = {"admin:tags:post"}, role = {"super_admin"})
    public Result<Void> createTag(@RequestBody TagFormParam param) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(param, tag);
        boolean b = tagService.save(tag);
        return b ? Result.success() : Result.error();
    }

    @PutMapping("updateTag/{id}")
    @ApiOperation("更新标签")
    @RequiresPermission(api = {"admin:tags:updateTag:{id}:put"}, role = {"super_admin"})
    public Result<Void> updateTag(@PathVariable Long id, @RequestBody TagFormParam param) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId, id).set(Tag::getColor, param.getColor()).set(Tag::getSlug, param.getSlug()).set(Tag::getName, param.getName()).set(Tag::getStatus, 1);
        boolean update = tagService.update(updateWrapper);

        return update ? Result.success() : Result.error();
    }


}
