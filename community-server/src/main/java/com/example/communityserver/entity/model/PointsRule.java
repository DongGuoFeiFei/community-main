package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分规则实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("points_rule")
@ApiModel(value = "PointsRule对象", description = "积分规则表")
public class PointsRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rule_id", type = IdType.AUTO)
    @ApiModelProperty(value = "规则ID", hidden = true)
    private Integer ruleId;

    @NotBlank(message = "规则代码不能为空")
    @ApiModelProperty(value = "规则代码(唯一标识)", required = true)
    private String ruleCode;

    @NotBlank(message = "规则名称不能为空")
    @ApiModelProperty(value = "规则名称", required = true)
    private String ruleName;

    @NotNull(message = "规则类型不能为空")
    @ApiModelProperty(value = "规则类型(1:获取积分 2:消费积分)", required = true)
    private Integer ruleType;

    @NotNull(message = "积分值不能为空")
    @ApiModelProperty(value = "积分值", required = true)
    private Integer pointsValue;

    @ApiModelProperty(value = "每日上限(0表示无限制)", example = "0")
    private Integer dailyLimit = 0;

    @ApiModelProperty(value = "总上限(0表示无限制)", example = "0")
    private Integer totalLimit = 0;

    @ApiModelProperty(value = "状态(0:禁用 1:启用)", example = "1")
    private Integer status = 1;

    @ApiModelProperty(value = "规则描述")
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;
}
