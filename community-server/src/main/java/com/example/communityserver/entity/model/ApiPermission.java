package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-20
 **/


@Data
@TableName("api_permission")
public class ApiPermission {
    @TableId(value = "api_id", type = IdType.AUTO)
    private Long apiId;

    private Long menuId;

    @TableField("api_name")
    private String apiName;

    @TableField("api_path")
    private String apiPath;

    @TableField("http_method")
    private String httpMethod;

    @TableField("perms")
    private String perms;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
