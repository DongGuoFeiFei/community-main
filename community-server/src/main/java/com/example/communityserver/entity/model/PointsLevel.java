package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 积分等级实体类
 */
@Data
@ApiModel(value = "PointsLevel对象", description = "积分等级配置")
public class PointsLevel {

    @TableId(value = "level_id", type = IdType.AUTO)
    @ApiModelProperty(value = "等级ID", example = "1")
    private Integer levelId;

    @ApiModelProperty(value = "等级名称", example = "初级会员")
    private String levelName;

    @ApiModelProperty(value = "等级图标")
    private String levelIcon;

    @ApiModelProperty(value = "经验值", example = "100")
    private Long minExperience;
    @ApiModelProperty(value = "经验值", example = "100")
    private Long maxExperience;

    @ApiModelProperty(value = "等级权益描述")
    private String privileges;

    @ApiModelProperty(value = "时间")
    private Date createTime;
}
