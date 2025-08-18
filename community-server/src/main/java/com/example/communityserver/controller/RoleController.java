package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.request.*;
import com.example.communityserver.security.core.Logical;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IRoleService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-12
 **/

@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiOperation("获取角色列表(分页)")
    @GetMapping("list")
    @RequiresPermission(role = {"super_admin","view_admin"}, logical = Logical.OR)
    public Result<Result.PageData<Role>> getRoleList(RoleSearchFormParam param) {
        IPage<Role> page = roleService.getRoleList(param);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }
    @ApiOperation("获取角色列表（不分页）")
    @GetMapping("getRoleList")
    @RequiresPermission(role = {"super_admin","view_admin"}, logical = Logical.OR)
    public Result<List<Role>> getRoleList() {
        List<Role> page = roleService.getRoleList();
        return page != null ? Result.success(page) : Result.error();
    }

    @ApiOperation("删除角色")
    @DeleteMapping()
    @RequiresPermission(role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> deleteRole(@RequestBody @Validated IdsListParam param) {
        ResponseCodeEnum codeEnum = roleService.deleteRole(param);
        return codeEnum == ResponseCodeEnum.SUCCESS ? Result.success() : Result.error(codeEnum.getCode(), codeEnum.getValue());
    }

    @ApiOperation("修改角色状态")
    @PutMapping("status")
    @RequiresPermission(role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> changeRoleStatus(@RequestBody IdStatusParam param) {
        ResponseCodeEnum codeEnum = roleService.changeRoleStatus(param);
        return codeEnum == ResponseCodeEnum.SUCCESS ? Result.success() : Result.error(codeEnum.getCode(), codeEnum.getValue());
    }

    @ApiOperation("获取角色详情")
    @GetMapping("{roleId}")
    @RequiresPermission(role = {"super_admin","view_admin"}, logical = Logical.OR)
    public Result<Role> getRoleDetail(@PathVariable Long roleId) {
        Role role = roleService.getRoleDetail(roleId);
        return role != null ? Result.success(role) : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

    @ApiOperation("创建角色")
    @PostMapping()
    @RequiresPermission(role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> createRole(@RequestBody AddRoleParam param) {
        Integer is = roleService.createRole(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

    @ApiOperation("更新角色")
    @PutMapping()
    @RequiresPermission(role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> updateRole(@RequestBody AddRoleParam param) {
        Integer is = roleService.updateRole(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }


    @ApiOperation("更新角色菜单权限")
    @PutMapping("menu")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermission(role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> updateRoleMenus(@RequestBody IdIdsParam param) {
        Integer is = roleService.updateRoleMenus(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

}
