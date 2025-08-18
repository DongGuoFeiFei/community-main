package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户违规行为实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_violations")
@ApiModel(value = "用户违规行为")
public class UserViolation {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "违规用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "违规类型", required = true)
    private Integer violationType;

    @ApiModelProperty(value = "违规次数")
    private Integer violationCount;

    @ApiModelProperty(value = "最近违规时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastViolationTime;

    @ApiModelProperty(value = "处罚状态(1:警告,2:限制发帖,3:封禁)")
    private Integer penaltyStatus;

    @ApiModelProperty(value = "处罚结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date penaltyEndTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;
}
