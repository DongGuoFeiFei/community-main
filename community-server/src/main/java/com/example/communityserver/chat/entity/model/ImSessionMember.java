package com.example.communityserver.chat.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/




@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("im_session_member")
@ApiModel(value = "会话成员实体类")
public class ImSessionMember {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "会话ID", example = "1")
    private Long sessionId;

    @ApiModelProperty(value = "用户ID", example = "456")
    private Long userId;

    @ApiModelProperty(value = "用户在群内的昵称", example = "技术小能手")
    private String nickname;

    @ApiModelProperty(value = "成员角色：1-普通成员，2-管理员，3-群主", example = "1")
    private Integer role;

    @ApiModelProperty(value = "是否免打扰：0-否，1-是", example = "0")
    private Integer isMuted;

    @ApiModelProperty(value = "逻辑删除标志", example = "0")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "加入时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "创建时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
