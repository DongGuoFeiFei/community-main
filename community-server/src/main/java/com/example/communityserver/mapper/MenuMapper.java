package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Menu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> selectUserMenus(Long userId);

    List<String> selectMenuKeysByUserId(Long userId);

    List<Menu> selectRoleMenus(Long roleId);
}
