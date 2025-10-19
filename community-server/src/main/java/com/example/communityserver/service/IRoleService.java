package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.*;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-12
 **/


public interface IRoleService extends IService<Role> {
    IPage<Role> getRoleList(RoleSearchFormParam param);

    List<Role> getRoleList();

    ResponseCodeEnum changeRoleStatus(IdStatusParam param);

    ResponseCodeEnum deleteRole(IdsListParam param);

    Role getRoleDetail(Long roleId);

    Integer createRole(AddRoleParam param);

    Integer updateRole(AddRoleParam param);

    Integer updateRoleMenus(IdIdsParam param);

    Integer updateRoleApis(IdIdsParam param);
}
