package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 收藏的枚举类型
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-21
 **/

@Getter
@AllArgsConstructor
public enum FavoriteTypeEnum {
    ARTICLE("article");

    private final String value;
}
