package com.example.communityserver.controller;

import com.example.communityserver.service.ITagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private ITagService tagService;



}
