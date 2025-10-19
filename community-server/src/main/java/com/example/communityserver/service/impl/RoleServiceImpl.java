package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.ApiPermission;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.model.RoleApi;
import com.example.communityserver.entity.request.*;
import com.example.communityserver.mapper.ApiPermissionMapper;
import com.example.communityserver.mapper.RoleApiMapper;
import com.example.communityserver.mapper.RoleMapper;
import com.example.communityserver.service.IRoleService;
import com.example.communityserver.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RoleApiMapper roleApiMapper;

    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Override
    public IPage<Role> getRoleList(RoleSearchFormParam param) {
        Page<Role> rolePage = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(param.getRoleName() != null, Role::getRoleName, param.getRoleName()).like(param.getRoleKey() != null, Role::getRoleKey, param.getRoleKey()).eq(param.getStatus() != null, Role::getStatus, param.getStatus());
        return roleMapper.selectPage(rolePage, queryWrapper);
    }

    @Override
    public List<Role> getRoleList() {
        return list();
    }

    @Override
    public ResponseCodeEnum changeRoleStatus(IdStatusParam param) {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper.eq(Role::getRoleId, param.getId()).set(Role::getStatus, param.getStatus());
        int updateCount = roleMapper.update(null, roleLambdaUpdateWrapper);
        return updateCount > 0 ? ResponseCodeEnum.SUCCESS : ResponseCodeEnum.FAILED;
    }

    @Override
    public ResponseCodeEnum deleteRole(IdsListParam param) {
        LambdaUpdateWrapper<Role> roleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        roleLambdaUpdateWrapper
                .in(Role::getRoleId, param.getIds())
                .eq(Role::getIsSystem, 0)
                .set(Role::getIsDel, 1);
        int updateCount = roleMapper.update(null, roleLambdaUpdateWrapper);
        return updateCount > 0 ? ResponseCodeEnum.SUCCESS : ResponseCodeEnum.FAILED;
    }

    @Override
    public Role getRoleDetail(Long roleId) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getRoleId, roleId);
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
        roleLambdaUpdateWrapper.eq(Role::getRoleId, param.getRoleId()).set(Role::getRoleName, param.getRoleName()).set(Role::getRoleKey, param.getRoleKey()).set(Role::getRoleSort, param.getRoleSort()).set(Role::getStatus, param.getStatus()).set(Role::getRemark, param.getRemark());
        return roleMapper.update(roleLambdaUpdateWrapper);
    }

    @Override
    public Integer updateRoleMenus(IdIdsParam param) {
        roleMapper.deleteRoleMenuByRoleId(param.getId());
        Integer is = roleMapper.insertRoleMenu(param);
        if (is > 0) {
            redisUtil.deleteObject(CacheKeyConstants.ROLE_MANAGE_MENUS + param.getId());
        }
        return is;
    }

    @Override
    public Integer updateRoleApis(IdIdsParam param) {
        roleApiMapper.delete(
                new LambdaQueryWrapper<RoleApi>()
                        .eq(RoleApi::getRoleId, param.getId())
        );

        Integer is = roleApiMapper.insertRoleApi(param);
        if (is > 0) {
            // 删除原有缓存
            Role role = roleMapper.selectById(param.getId());
            String key = CacheKeyConstants.PERMISSION_IDENTIFIER_ROLE + role.getRoleKey();
            redisUtil.deleteObject(key);

            // 添加缓存
            List<String> collect = apiPermissionMapper.selectList(
                    new LambdaQueryWrapper<ApiPermission>()
                            .in(param.getIds().size() > 0, ApiPermission::getApiId, param.getIds())
            ).stream().map(ApiPermission::getPerms).collect(Collectors.toList());
            redisUtil.setCacheList(key, collect);
            redisUtil.removeExpire(key);
        }
        return is;

    }

}
