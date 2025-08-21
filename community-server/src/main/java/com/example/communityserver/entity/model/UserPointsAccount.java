package com.example.communityserver.entity.model;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/



/**
 * 用户积分账户实体类
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_points_account")
@ApiModel(value = "UserPointsAccount对象", description = "用户积分账户表")
public class UserPointsAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "account_id", type = IdType.AUTO)
    @ApiModelProperty(value = "积分账户ID", hidden = true)
    private Long accountId;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "关联的用户ID", required = true)
    private Long userId;

    @Min(value = 0, message = "总积分不能小于0")
    @ApiModelProperty(value = "累计获得的总积分", example = "0")
    private Long totalPoints = 0L;

    @Min(value = 0, message = "可用积分不能小于0")
    @ApiModelProperty(value = "当前可用积分", example = "0")
    private Long availablePoints = 0L;

    @Min(value = 0, message = "已消费积分不能小于0")
    @ApiModelProperty(value = "已消费积分", example = "0")
    private Long consumedPoints = 0L;

    @Min(value = 0, message = "冻结积分不能小于0")
    @ApiModelProperty(value = "冻结积分", example = "0")
    private Long frozenPoints = 0L;

    @Min(value = 1, message = "等级ID不能小于1")
    @ApiModelProperty(value = "当前等级ID", example = "1")
    private Integer levelId = 1;

    @Min(value = 0, message = "经验值不能小于0")
    @ApiModelProperty(value = "当前经验值", example = "0")
    private Long experience = 0L;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间", hidden = true)
    private Date lastUpdateTime;

    // 关联实体
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private User user;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private PointsLevel pointsLevel;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<PointsLog> pointsLogs;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<PointsConsumption> consumptions;
}
