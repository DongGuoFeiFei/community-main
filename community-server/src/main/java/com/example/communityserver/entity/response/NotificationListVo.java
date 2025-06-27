package com.example.communityserver.entity.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/

@Data
public class NotificationListVo {
    private Long notificationId;
    private Long userId;
    private String type;
    private String color;
    private Long parentSourceId;
    private Long sonSourceId;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
}
