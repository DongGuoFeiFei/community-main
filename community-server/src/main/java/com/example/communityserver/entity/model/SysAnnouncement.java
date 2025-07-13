package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_announcement")
public class SysAnnouncement {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "公告标题不能为空")
    @Size(max = 100, message = "公告标题不能超过100个字符")
    private String title;

    @NotBlank(message = "公告内容不能为空")
    private String content;

    @NotBlank(message = "发布人不能为空")
    @Size(max = 50, message = "发布人不能超过50个字符")
    private String publisher;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    private Integer status;
    private Integer priority;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("end_time")
    private LocalDateTime endTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String remark;

    @TableLogic
    private Integer deleted;
}
