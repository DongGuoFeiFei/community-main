package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.Menu;
import com.example.communityserver.entity.model.Role;
import com.example.communityserver.entity.response.UserMenuTree;
import com.example.communityserver.mapper.MenuMapper;
import com.example.communityserver.mapper.RoleMapper;
import com.example.communityserver.security.util.SecurityUtils;
import com.example.communityserver.service.IMenuService;
import com.example.communityserver.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<UserMenuTree> getUserMenuTree() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 1. 获取用户所有角色
        assert loginUser != null;
        List<String> roleKeys = loginUser.getRoles();
        LambdaQueryWrapper<Role> roleQuery = new LambdaQueryWrapper<>();
        roleQuery.in(Role::getRoleKey, roleKeys);
        List<Role> roles = roleMapper.selectList(roleQuery);

        // 2. 收集所有菜单（保持角色独立缓存）
        Set<Menu> mergedMenus = new LinkedHashSet<>(); // 保持插入顺序
        for (Role role : roles) {
            // 每个角色独立缓存
            String roleCacheKey = CacheKeyConstants.ROLE_MANAGE_MENUS + role.getRoleId();
            List<Menu> cachedRoleMenus = redisUtil.getCacheList(roleCacheKey);

            if (!cachedRoleMenus.isEmpty()) {
                // 如果有缓存，直接展开树结构获取所有菜单项
                mergedMenus.addAll(cachedRoleMenus);
                redisUtil.expire(roleCacheKey, 1, TimeUnit.DAYS);
            } else {
                // 无缓存则查询数据库
                List<Menu> roleMenus = menuMapper.selectRoleMenus(role.getRoleId());
                mergedMenus.addAll(roleMenus);

                // 缓存该角色的完整菜单树
                redisUtil.setCacheList(roleCacheKey, roleMenus);
                redisUtil.expire(roleCacheKey, 1, TimeUnit.DAYS);
            }
        }

        // 3. 构建最终合并后的菜单树
        return buildMenuTree(new ArrayList<>(mergedMenus));
    }

    @Override
    public List<UserMenuTree> getMenuTree() {
        List<Menu> menuLists = redisUtil.getCacheList(CacheKeyConstants.ROLE_MANAGE_MENUS + "allMenu");
        if (menuLists.isEmpty()) {
            menuLists = list();
            redisUtil.setCacheList(CacheKeyConstants.ROLE_MANAGE_MENUS + "allMenu", menuLists);
        }
        redisUtil.expire(CacheKeyConstants.ROLE_MANAGE_MENUS + "allMenu", 1, TimeUnit.DAYS);
        return buildMenuTree(menuLists);
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        List<Menu> menus = menuMapper.selectRoleMenus(roleId);
        List<Menu> lowestNodeMenus = toLowestNodeMenus(menus);
        return lowestNodeMenus.stream().map(Menu::getMenuId).collect(Collectors.toList());
    }

    private List<UserMenuTree> buildMenuTree(List<Menu> menus) {
        // 1. 先找出所有顶级菜单(parentId=0或null)
        List<Menu> topMenus = menus.stream().filter(menu -> menu.getParentId() == null || menu.getParentId() == 0).sorted(Comparator.comparing(Menu::getOrderNum)).collect(Collectors.toList());

        // 2. 递归构建树形结构
        return topMenus.stream().map(menu -> convertToTree(menu, menus)).collect(Collectors.toList());
    }

    private List<Menu> toLowestNodeMenus(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集所有父菜单ID
        Set<Long> parentIds = menus.stream().map(Menu::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());

        // 筛选出那些menuId不在parentIds中的菜单
        return menus.stream().filter(menu -> !parentIds.contains(menu.getMenuId())).collect(Collectors.toList());
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
        List<Menu> children = allMenus.stream().filter(m -> menu.getMenuId().equals(m.getParentId())).sorted(Comparator.comparing(Menu::getOrderNum)).collect(Collectors.toList());

        if (!children.isEmpty()) {
            List<UserMenuTree> childTrees = children.stream().map(child -> convertToTree(child, allMenus)).collect(Collectors.toList());
            tree.setChildren(childTrees);
        }
        return tree;
    }
}
