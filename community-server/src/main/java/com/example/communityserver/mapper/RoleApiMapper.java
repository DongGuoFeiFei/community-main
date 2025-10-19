package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.RoleApi;
import com.example.communityserver.entity.request.IdIdsParam;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-20
 **/


public interface RoleApiMapper extends BaseMapper<RoleApi> {
    Integer insertRoleApi(@Param("param") IdIdsParam param);
}
