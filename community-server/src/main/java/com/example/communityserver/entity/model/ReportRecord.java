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
 * 举报记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("report_records")
@ApiModel(value = "举报记录")
public class ReportRecord {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "举报人ID", required = true)
    private Long reporterId;

    @ApiModelProperty(value = "被举报内容ID", required = true)
    private Long contentId;

    @ApiModelProperty(value = "内容类型(1:文章,2:评论,3:回复等)", required = true)
    private Integer contentType;

    @ApiModelProperty(value = "举报类型(1:垃圾广告,2:色情低俗,3:政治敏感,4:侵权,5:其他)", required = true)
    private Integer reportType;

    @ApiModelProperty(value = "举报描述")
    private String description;

    @ApiModelProperty(value = "证据(图片/视频等URL,JSON格式存储)")
    private String evidence;

    @ApiModelProperty(value = "处理状态(0:待处理,1:已处理,2:已忽略)")
    private Integer status;

    @ApiModelProperty(value = "处理人ID")
    private Long handlerId;

    @ApiModelProperty(value = "处理结果(1:删除内容,2:警告用户,3:封禁用户,4:无违规)")
    private Integer handleResult;

    @ApiModelProperty(value = "处理备注")
    private String handleRemark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    /**
     * @Description: 创建时最基础数据
     * @Param: [reporterId, contentId, contentType, reportType, description]
     * @return:
     * @Author: DongGuo
     */

    public ReportRecord(Long reporterId, Long contentId, Integer contentType, Integer reportType, String description) {
        this.reporterId = reporterId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.reportType = reportType;
        this.description = description;
    }
}
