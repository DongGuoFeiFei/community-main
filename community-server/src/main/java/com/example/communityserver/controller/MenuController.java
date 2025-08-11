package com.example.communityserver.controller;

import com.example.communityserver.service.IMenuService;
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
 * @create: 2025-08-11
 **/

@Api(tags = "菜单管理")
@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;


}
