package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 角色权限关联表
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-20
 **/
@Data
@TableName("role_api")
public class RoleApi {
    private Long apiId;
    private Long roleId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
