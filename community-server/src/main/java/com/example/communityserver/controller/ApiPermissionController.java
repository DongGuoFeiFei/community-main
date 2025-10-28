package com.example.communityserver.controller;

import com.example.communityserver.entity.model.ApiPermission;
import com.example.communityserver.core.security.aop.RequiresPermission;
import com.example.communityserver.service.IApiPermissionService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * api权限内容
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/
@Slf4j
@Api(tags = "api权限内容")
@RestController
@RequestMapping("apiPermission")
public class ApiPermissionController {
    @Autowired
    private IApiPermissionService apiPermissionService;

    @ApiOperation("获取API树")
    @GetMapping("tree")
    @RequiresPermission(api = {"apiPermission:tree:get"}, role = {"super_admin"})
    public Result<List<ApiPermission>> getApiTree() {
        List<ApiPermission> apiPermissionList = apiPermissionService.getApiTree();
        return apiPermissionList != null ? Result.success(apiPermissionList) : Result.error();
    }


    @ApiOperation("获取角色API权限")
    @GetMapping("{roleId}")
    @RequiresPermission(api = {"apiPermission:role:{id}:get"}, role = {"super_admin"})
    public Result<List<Long>> getRoleApis(@PathVariable Long roleId) {
        List<Long> list = apiPermissionService.getRoleApis(roleId);
        return list != null ? Result.success(list) : Result.error();
    }



}
