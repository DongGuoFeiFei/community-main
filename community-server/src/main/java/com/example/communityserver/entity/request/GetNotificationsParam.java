package com.example.communityserver.entity.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 获取通知参数
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/
@Data
public class GetNotificationsParam {
    @NotNull(message = "页码不能为空")
    private Integer page;

    @NotNull(message = "每页大小不能为空")
    private Integer size;

    @Pattern(regexp = "^(like|comment|follow|system|reply)?$")
    private String type;

    private Boolean isRead;
}