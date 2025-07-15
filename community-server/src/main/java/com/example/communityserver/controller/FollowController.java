package com.example.communityserver.controller;

import com.example.communityserver.entity.request.IdStatusParam;
import com.example.communityserver.service.IFollowService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-15
 **/

@Api(tags = "关注")
@RestController
@RequestMapping("follow")
public class FollowController {
    @Autowired
    private IFollowService followService;

    @ApiOperation("添加和取消关注")
    @PostMapping("followAuthor")
    public Result<Void> followAuthor(@RequestBody IdStatusParam param) {
//        followService.followAuthor(param);
        return  null;
    }


}
