package com.example.communityserver.controller;

import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/getAllTags")
    public Result<List<Tag>> getAllTags() {
        List<Tag> list = tagService.list();
        return list != null ? Result.success(list) : Result.error();
    }

//    @ApiOperation("热门标签")
//    @RequestMapping("/getPopularTags")
//    public Result<List<Tag>> getPopularTags() {
//        // TODO: 2025/7/5 根据用户喜好推荐热门标签
//
//    }


}
