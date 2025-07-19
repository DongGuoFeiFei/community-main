package com.example.communityserver.controller;

import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.CreateTagParam;
import com.example.communityserver.entity.response.TagVo;
import com.example.communityserver.mapping.TagMapping;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    public Result<List<TagVo>> getAllTags() {
        List<Tag> list = tagService.list();
        ArrayList<TagVo> tagVos = new ArrayList<>();
        TagMapping.INSTANCE.toListTagVo(list, tagVos);
        return list != null ? Result.success(tagVos) : Result.error();
    }

    @ApiOperation("热门标签")
    @GetMapping("/getPopularTags")
    public Result<List<TagVo>> getPopularTags() {
        // TODO: 2025/7/5 根据用户喜好推荐热门标签
        List<TagVo> tags = tagService.getPopularTags();
        return tags != null ? Result.success(tags) : Result.error();
    }

    @ApiOperation("创建新标签")
    @PostMapping("/createTag")
    public Result<TagVo> createTag(@RequestBody CreateTagParam param) {
        // TODO: 2025/7/5 添加敏感词检测 标签
        Tag tag = new Tag();
        tag.setCreatorId(SecurityUtils.getLoginUserId());
        tag.setName(param.getName());
        tag.setColor(param.getColor());
        if (StringUtil.isBlank(param.getSlug())){
            param.setSlug(param.getName());
        }
        tag.setSlug(param.getSlug());
        tag.setCreateCount(1L);
        boolean b = tagService.save(tag);
        TagVo tagVo = new TagVo();
        TagMapping.INSTANCE.toTagVo(tag, tagVo);
        return Result.success(tagVo);
    }


}
