package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.CreateTagParam;
import com.example.communityserver.entity.response.TagVo;
import com.example.communityserver.mapping.TagMapping;
import com.example.communityserver.security.core.Logical;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/

@Api(tags = "标签管理")
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private ITagService tagService;

    @ApiOperation("所有标签")
    @GetMapping("/getAllTags")
    @RequiresPermission(api = {"tag:getAllTags:get"} ,role = {"super_admin"}, logical = Logical.OR)
    public Result<List<TagVo>> getAllTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Tag::getStatus, 2).orderByDesc(Tag::getCreateCount);
        List<Tag> list = tagService.list(queryWrapper);
        ArrayList<TagVo> tagVos = new ArrayList<>();
        TagMapping.INSTANCE.toListTagVo(list, tagVos);
        return list != null ? Result.success(tagVos) : Result.error();
    }

    @ApiOperation("热门标签")
    @GetMapping("/getPopularTags")
    @RequiresPermission(api = {"tag:getPopularTags:get"} ,role = {"super_admin"}, logical = Logical.OR)
    public Result<List<TagVo>> getPopularTags() {
        // TODO: 2025/7/5 根据用户喜好推荐热门标签
        List<TagVo> tags = tagService.getPopularTags();
        return tags != null ? Result.success(tags) : Result.error();
    }

    @ApiOperation("创建新标签")
    @PostMapping("/createTag")
    @RequiresPermission(api = {"tag:createTag:post"} ,role = {"super_admin"}, logical = Logical.OR)
    public Result<TagVo> createTag(@RequestBody CreateTagParam param) {
        // TODO: 2025/7/5 添加敏感词检测 标签
        TagVo tagVo = tagService.createTag(param);
        return tagVo != null ? Result.success(tagVo) : Result.error("创建失败，该标签已经存在或名称不当。");
    }

}
