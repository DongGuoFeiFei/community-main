package com.example.communityserver.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 菜单数据传输对象
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-17
 **/


@Data
@EqualsAndHashCode(callSuper = false)
public class MenuDto {

    private Long menuId;

    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    private String path;

    private String component;

    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    @NotNull(message = "显示状态不能为空")
    private Integer visible;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String perms;

    private String icon;

    private String remark;

    private Integer isExternal;

    private String target;

}
