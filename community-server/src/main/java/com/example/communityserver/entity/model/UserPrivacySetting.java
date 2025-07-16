package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-16
 **/


@Data
@TableName("user_privacy_setting")
public class UserPrivacySetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer profileVisibility;
    private Integer postVisibility;
    private Integer commentVisibility;
    private Integer messageReceive;
    private Integer searchVisibility;
    private Integer activityStatus;
    private Date createTime;
    private Date updateTime;
}