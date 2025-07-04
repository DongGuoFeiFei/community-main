package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;
    private Long followerId;//博主
    private Long followingId;//观众
    private String createAt;
}
