package com.example.communityserver.controller;

import com.example.communityserver.service.ILikesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/

@Api(tags = "点赞管理")
@RestController
@RequestMapping("/like")
public class LikesController {
    @Autowired
    private ILikesService likesService;


}
