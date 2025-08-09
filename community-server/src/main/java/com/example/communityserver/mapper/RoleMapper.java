package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Role;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeysByUserId(Long userId);

    List<Role> selectUserRoles(Long userId);
}
