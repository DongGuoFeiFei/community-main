package com.example.communityserver.entity.response;

import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 通知通用返回实体
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/

@Data
public class NotificationListVo {
    /**
     * 通知ID
     */
    private Long notificationId;
    /**
     * 通知类型
     */
    private ActiveTypeEnum type;
    /**
     * 通知标题（用于前端显示）
     */
    private String title;
    /**
     * 通知内容（根据不同类型有不同的内容）
     */
    private String content;
    /**
     * 通知图标/颜色
     */
    private String icon;
    private String color;
    /**
     * 相关用户信息（触发通知的用户）
     */
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    /**
     * 相关资源信息（根据不同类型有不同含义）
     */
    private Long sourceId;          // 主资源ID（如文章ID）
    private String sourceTitle;      // 主资源标题（如文章标题）
    private Long relatedId;         // 关联资源ID（如评论ID）
    private String relatedContent;  // 关联资源内容（如评论内容）
    /**
     * 状态信息
     */
    private Boolean isRead;
    private Boolean isDeleted;
    /**
     * 时间信息
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    /**
     * 额外数据（JSON格式，用于存储特殊字段）
     */
    private Map<String, Object> extraData;
}
