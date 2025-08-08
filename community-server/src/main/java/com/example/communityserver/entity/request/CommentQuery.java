package com.example.communityserver.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-07
 **/


@Data
@ApiModel("评论查询参数")
public class CommentQuery {
    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("状态: 0-待审核, 1-已通过, 2-已拒绝, 3-已删除")
    private Integer status;
    @ApiModelProperty("开始时间 (格式: yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty("结束时间 (格式: yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty("页码 (从1开始)")
    private Integer page;
    @ApiModelProperty("每页数量")
    private Integer size;
}
