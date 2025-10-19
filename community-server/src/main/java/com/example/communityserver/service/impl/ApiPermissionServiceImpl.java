package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ApiPermission;
import com.example.communityserver.entity.model.RoleApi;
import com.example.communityserver.mapper.ApiPermissionMapper;
import com.example.communityserver.mapper.RoleApiMapper;
import com.example.communityserver.service.IApiPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 接口权限表 服务实现类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/

@Service
public class ApiPermissionServiceImpl extends ServiceImpl<ApiPermissionMapper, ApiPermission> implements IApiPermissionService {
    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Autowired
    private RoleApiMapper roleApiMapper;

    @Override
    public List<ApiPermission> getApiTree() {
        return list();
    }

    @Override
    public List<Long> getRoleApis(Long roleId) {
        return roleApiMapper.selectList(
                new LambdaQueryWrapper<RoleApi>()
                        .eq(RoleApi::getRoleId, roleId)
        )
                .stream()
                .map(RoleApi::getApiId)
                .collect(Collectors.toList());
    }
}
