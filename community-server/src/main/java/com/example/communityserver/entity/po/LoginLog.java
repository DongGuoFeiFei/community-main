package com.example.communityserver.entity.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 登录日志
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-07
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("login_log")
@ApiModel("登录日志")
public class LoginLog {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "日志ID", example = "1000001")
    private Long logId;

    @ApiModelProperty(value = "用户ID", example = "1001")
    private Long userId;

    @ApiModelProperty(value = "登录时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String loginLastTime;

    @ApiModelProperty(value = "登录IP", example = "192.168.1.1")
    private String loginIp;

    @ApiModelProperty(value = "登录地点", example = "中国北京")
    private String loginLocation;
}