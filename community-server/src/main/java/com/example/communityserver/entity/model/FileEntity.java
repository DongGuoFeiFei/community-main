package com.example.communityserver.entity.model;

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
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-29
 **/


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("file")
@ApiModel(value = "文件实体类")
public class FileEntity {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "文件ID", example = "1")
    private Long fileId;
    @ApiModelProperty(value = "上传文件用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "文件原始名称", example = "example.pdf")
    private String fileOriginalName;

    @ApiModelProperty(value = "系统分配名称", example = "a1b2c3d4.pdf")
    private String fileAutoName;

    @ApiModelProperty(value = "本地存储路径", example = "a1b2c3d4.pdf")
    private String storageUrl;
    @ApiModelProperty(value = "前端访问路径", example = "a1b2c3d4.pdf")
    private String accessUrl;

    @ApiModelProperty(value = "文件上传时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String uploadTime;
}