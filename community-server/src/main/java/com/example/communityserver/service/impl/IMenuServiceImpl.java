package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.Menu;
import com.example.communityserver.entity.response.UserMenuTree;
import com.example.communityserver.mapper.MenuMapper;
import com.example.communityserver.service.IMenuService;
import com.example.communityserver.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-11
 **/
@Slf4j
@Service
public class IMenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<UserMenuTree> getUserMenuTree(Long userId) {
        List<UserMenuTree> menuTrees = redisUtil.getCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + userId);
        if (menuTrees.isEmpty()) {
            List<Menu> menus = menuMapper.selectUserMenus(userId);
            menuTrees = buildMenuTree(menus);
            redisUtil.setCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + userId, menuTrees);
        }
        redisUtil.expire(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + userId, 1, TimeUnit.DAYS);
        return menuTrees;
    }

    @Override
    public List<UserMenuTree> getMenuTree() {
        List<UserMenuTree> menuTrees = redisUtil.getCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + "menu");
        if (menuTrees.isEmpty()) {
            List<Menu> menus = list();
            menuTrees = buildMenuTree(menus);
            redisUtil.setCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + "menu", menuTrees);
        }
        redisUtil.expire(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + "menu", 1, TimeUnit.DAYS);
        return menuTrees;

    }

    @Override
    public List<UserMenuTree> getRoleMenus(Long roleId) {
        List<UserMenuTree> menuTrees = redisUtil.getCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + roleId);
        if (menuTrees.isEmpty()) {
            List<Menu> menus = menuMapper.selectUserMenus(roleId);
            menuTrees = buildMenuTree(menus);
            redisUtil.setCacheList(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + roleId, menuTrees);
        }
        redisUtil.expire(CacheKeyConstants.LOGIN_USER_MANAGE_MENUS + roleId, 1, TimeUnit.DAYS);
        return menuTrees;
    }

    private List<UserMenuTree> buildMenuTree(List<Menu> menus) {
        // 1. 先找出所有顶级菜单(parentId=0或null)
        List<Menu> topMenus = menus.stream()
                .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());

        // 2. 递归构建树形结构
        return topMenus.stream()
                .map(menu -> convertToTree(menu, menus))
                .collect(Collectors.toList());
    }

    private UserMenuTree convertToTree(Menu menu, List<Menu> allMenus) {
        UserMenuTree tree = new UserMenuTree();
        tree.setPath(menu.getPath());
        tree.setComponent(menu.getComponent());
        tree.setMenuId(menu.getMenuId());
        tree.setMenuType(menu.getMenuType());
        tree.setMenuName(menu.getMenuName());
        tree.setIcon(menu.getIcon());

        // 设置meta信息
        UserMenuTree.Meta meta = new UserMenuTree.Meta();
        meta.setTitle(menu.getMenuName());
        meta.setIcon(menu.getIcon());
        tree.setMeta(meta);

        // 查找并设置子菜单
        List<Menu> children = allMenus.stream()
                .filter(m -> menu.getMenuId().equals(m.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            List<UserMenuTree> childTrees = children.stream()
                    .map(child -> convertToTree(child, allMenus))
                    .collect(Collectors.toList());
            tree.setChildren(childTrees);
        }

        return tree;
    }
}
