package com.example.communityserver.controller;

import com.example.communityserver.entity.response.UserMenuTree;
import com.example.communityserver.security.core.Logical;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IMenuService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @ApiOperation("获取角色管理菜单")
    @GetMapping("getUserMenuTree")
    @RequiresPermission(value = {"super_admin", "system_admin"}, logical = Logical.OR)
    public Result<List<UserMenuTree>> getUserMenuTree() {
        List<UserMenuTree> listVo = menuService.getUserMenuTree();
        return listVo != null ? Result.success(listVo) : Result.error("获取失败。");
    }

    @ApiOperation("获取角色管理菜单")
    @GetMapping("tree")
    @RequiresPermission(value = {"super_admin"}, logical = Logical.OR)
    public Result<List<UserMenuTree>> getMenuTree() {
        List<UserMenuTree> listVo = menuService.getMenuTree();
        return listVo != null ? Result.success(listVo) : Result.error("获取失败。");
    }

    @ApiOperation("获取角色菜单权限id")
    @GetMapping("{roleId}")
    @RequiresPermission(value = {"super_admin"}, logical = Logical.OR)
    public Result<List<Long>> getRoleMenus(@PathVariable Long roleId) {
        List<Long> listVo = menuService.getRoleMenuIds(roleId);
        return listVo != null ? Result.success(listVo) : Result.error("获取失败。");
    }

}
