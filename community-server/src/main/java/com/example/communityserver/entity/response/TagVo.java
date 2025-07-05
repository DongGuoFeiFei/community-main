package com.example.communityserver.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-05
 **/

@Data
public class TagVo {
    private Long id;

    private String name;

    private String slug;

    private String description;

    private String icon;

    private String color;

    private Integer status;
}
