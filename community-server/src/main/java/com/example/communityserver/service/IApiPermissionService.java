package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.ApiPermission;

import java.util.List;

/**
 * <p>
 * 公告
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/


public interface IApiPermissionService extends IService<ApiPermission> {

    List<ApiPermission> getApiTree();

    List<Long> getRoleApis(Long roleId);
}
