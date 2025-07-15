package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-03
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("follows")
@ApiModel(value = "关注表")
public class Follow {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long followerId;//博主
    private Long followingId;//观众
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
}
