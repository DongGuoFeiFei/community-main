package com.example.communityserver.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-22
 **/

@Data
public class EditorArticlesVo {
    @ApiModelProperty(value = "文章ID", example = "1234567890")
    private String id;
    @ApiModelProperty(value = "文章标题", example = "Spring Boot实战教程")
    private String title;
    @ApiModelProperty(value = "文章内容（Markdown格式）", example = "## 这是文章内容")
    private String content;
    @ApiModelProperty(value = "文章状态（0-已发布，1-草稿）", example = "0")
    private Integer status;
    @ApiModelProperty(value = "封面文件ID", example = "file_123456")
    private String fileId;
    @ApiModelProperty(value = "封面访问URL（完整路径）", example = "http://yourdomain.com/files/access/cover.jpg")
    private String coverUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    private String createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", example = "2023-10-02 15:30:00")
    private String updateTime;
}
