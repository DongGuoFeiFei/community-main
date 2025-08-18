package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.Menu;
import com.example.communityserver.entity.request.MenuDto;
import com.example.communityserver.entity.request.MenuSearchParam;
import com.example.communityserver.entity.response.UserMenuTree;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-11
 **/


public interface IMenuService extends IService<Menu> {
    List<UserMenuTree> getUserMenuTree();

    List<UserMenuTree> getMenuTree();

    List<Long> getRoleMenuIds(Long roleId);

    IPage<Menu> getMenuList(MenuSearchParam param);

    Boolean deleteMenu(Long menuId);

    Boolean addMenu(MenuDto dto);

    Boolean updateMenu(MenuDto dto);
}
