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
 * 积分消费实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("points_consumption")
@ApiModel(value = "PointsConsumption对象", description = "积分消费表")
public class PointsConsumption implements Serializable {

    private static final long serialVersionUID = 8755443163946017925L;

    @TableId(value = "consumption_id", type = IdType.AUTO)
    @ApiModelProperty(value = "消费ID", hidden = true)
    private Long consumptionId;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull(message = "账户ID不能为空")
    @ApiModelProperty(value = "积分账户ID", required = true)
    private Long accountId;

    @NotNull(message = "使用积分不能为空")
    @ApiModelProperty(value = "使用的积分", required = true)
    private Integer pointsUsed;

    @NotBlank(message = "项目ID不能为空")
    @ApiModelProperty(value = "消费项目ID", required = true)
    private String itemId;

    @NotBlank(message = "项目类型不能为空")
    @ApiModelProperty(value = "消费项目类型", required = true)
    private String itemType;

    @NotBlank(message = "项目名称不能为空")
    @ApiModelProperty(value = "消费项目名称", required = true)
    private String itemName;

    @ApiModelProperty(value = "状态(0:处理中 1:已完成 2:已取消)", example = "0")
    private Integer status = 0;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "完成时间")
    private Date completeTime;

    // 关联实体
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private UserPointsAccount account;
}
