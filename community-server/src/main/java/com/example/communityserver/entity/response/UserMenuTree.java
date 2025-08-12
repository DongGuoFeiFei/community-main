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
    private String path;
    private String component;
    private Meta meta;
    private List<UserMenuTree> children;

    @Data
    public static class Meta {
        private String title;
        private String icon;
    }
}
