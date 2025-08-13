package com.example.communityserver.entity.response;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-12
 **/

@Data
public class UserMenuTree {
    private Long menuId;
    private String menuName;
    private String path;
    private String menuType;
    private String component;
    private String icon;
    private Meta meta;
    private List<UserMenuTree> children;

    @Data
    public static class Meta {
        private String title;
        private String icon;
    }
}
