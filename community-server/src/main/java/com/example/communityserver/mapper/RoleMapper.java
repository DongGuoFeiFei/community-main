package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.request.IdIdsParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeysByUserId(Long userId);

    List<Role> selectUserRoles(Long userId);

    Integer deleteRoleMenuByRoleId(Long id);

    Integer insertRoleMenu(@Param("param") IdIdsParam param);
}
