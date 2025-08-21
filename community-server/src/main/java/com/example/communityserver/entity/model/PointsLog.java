package com.example.communityserver.entity.model;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/




@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_points_log")
@ApiModel(value = "PointsLog对象", description = "用户积分记录表")
public class PointsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    @ApiModelProperty(value = "记录ID", hidden = true)
    private Long logId;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull(message = "账户ID不能为空")
    @ApiModelProperty(value = "积分账户ID", required = true)
    private Long accountId;

    @ApiModelProperty(value = "关联的规则ID")
    private Integer ruleId;

    @ApiModelProperty(value = "规则代码")
    private String ruleCode;

    @NotNull(message = "变动积分不能为空")
    @ApiModelProperty(value = "变动积分(正数为增加，负数为减少)", required = true)
    private Integer changePoints;

    @NotNull(message = "余额不能为空")
    @ApiModelProperty(value = "变动后余额", required = true)
    private Long balance;

    @NotNull(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型(1:获取 2:消费 3:冻结 4:解冻 5:调整)", required = true)
    private Integer operationType;

    @ApiModelProperty(value = "来源ID(如帖子ID、评论ID等)")
    private String sourceId;

    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态(0:无效 1:有效 2:已撤销)", example = "1")
    private Integer status = 1;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    // 关联实体
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private UserPointsAccount account;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private PointsRule rule;
}
