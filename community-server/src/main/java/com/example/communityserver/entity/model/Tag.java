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
 * 标签
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-02
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tag")
@ApiModel(value = "Tag对象", description = "标签表")
public class Tag {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "标签ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "标签名称", example = "Java")
    private String name;

    @ApiModelProperty(value = "标签别名(用于URL)", example = "java")
    private String slug;

    @ApiModelProperty(value = "标签描述", example = "Java编程语言相关")
    private String description;

    @ApiModelProperty(value = "标签图标URL", example = "https://example.com/icons/java.png")
    private String icon;

    @ApiModelProperty(value = "标签颜色", example = "#FF5722")
    private String color;

    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    @ApiModelProperty(value = "更新时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updatedAt;

    @ApiModelProperty(value = "是否删除(0:未删除,1:已删除)", example = "0")
    private Integer isDel;
}