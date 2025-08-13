package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.request.AddRoleParam;
import com.example.communityserver.entity.request.IdStatusParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.request.RoleSearchFormParam;
import com.example.communityserver.mapper.RoleMapper;
import com.example.communityserver.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-12
 **/

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public IPage<Role> getRoleList(RoleSearchFormParam param) {
        Page<Role> rolePage = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(param.getRoleName() != null, Role::getRoleName, param.getRoleName())
                .like(param.getRoleKey() != null, Role::getRoleKey, param.getRoleKey())
                .eq(param.getStatus() != null, Role::getStatus, param.getStatus());
        return roleMapper.selectPage(rolePage, queryWrapper);
    }

    @Override
    public ResponseCodeEnum changeRoleStatus(IdStatusParam param) {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper
                .eq(Role::getRoleId, param.getId())
                .set(Role::getStatus, param.getStatus());
        int updateCount = roleMapper.update(null, roleLambdaUpdateWrapper);
        return updateCount > 0 ? ResponseCodeEnum.SUCCESS : ResponseCodeEnum.FAILED;
    }

    @Override
    public ResponseCodeEnum deleteRole(IdsListParam param) {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper
                .in(Role::getRoleId, param.getIds())
                .set(Role::getIsDel, 1);
        int updateCount = roleMapper.update(null, roleLambdaUpdateWrapper);
        return updateCount > 0 ? ResponseCodeEnum.SUCCESS : ResponseCodeEnum.FAILED;
    }

    @Override
    public Role getRoleDetail(Long roleId) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper
                .eq(Role::getRoleId, roleId);
        return roleMapper.selectById(roleId);
    }

    @Override
    public Integer createRole(AddRoleParam param) {
        Role role = new Role(param.getRoleName(), param.getRoleKey(), param.getRoleSort(), param.getStatus(), param.getRemark());
        return roleMapper.insert(role);
    }

    @Override
    public Integer updateRole(AddRoleParam param) {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper
                .eq(Role::getRoleId, param.getRoleId())
                .set(Role::getRoleName, param.getRoleName())
                .set(Role::getRoleKey, param.getRoleKey())
                .set(Role::getRoleSort, param.getRoleSort())
                .set(Role::getStatus, param.getStatus())
                .set(Role::getRemark, param.getRemark());
        return roleMapper.update(roleLambdaUpdateWrapper);
    }

}
