package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-16
 **/


@Data
@TableName("sys_config")
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String configKey;
    private String configValue;
    private Integer configType;
    private String description;
    private Date createTime;
    private Date updateTime;
}
