package com.example.communityserver.chat.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会话详情响应
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Data
@ApiModel("会话详情")
public class SessionDetailResponse {

    @ApiModelProperty("会话ID")
    private Long id;

    @ApiModelProperty("会话类型：1-私聊 2-群聊")
    private Integer type;

    @ApiModelProperty("会话名称")
    private String name;

    @ApiModelProperty("会话头像")
    private String avatar;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ApiModelProperty("成员列表")
    private List<MemberInfo> members;

    @ApiModelProperty("对方用户信息（私聊专用）")
    private PeerUserInfo peer;

    /**
     * 成员信息
     */
    @Data
    @ApiModel("成员信息")
    public static class MemberInfo {
        
        @ApiModelProperty("用户ID")
        private Long userId;

        @ApiModelProperty("昵称")
        private String nickname;

        @ApiModelProperty("头像")
        private String avatar;

        @ApiModelProperty("角色：1-普通 2-管理员 3-群主")
        private Integer role;

        @ApiModelProperty("加入时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime joinedAt;
    }

    /**
     * 对方用户信息（私聊）
     */
    @Data
    @ApiModel("对方用户信息")
    public static class PeerUserInfo {
        
        @ApiModelProperty("用户ID")
        private Long userId;

        @ApiModelProperty("昵称")
        private String nickname;

        @ApiModelProperty("头像")
        private String avatar;

        @ApiModelProperty("个性签名")
        private String signature;

        @ApiModelProperty("是否在线")
        private Boolean isOnline;
    }
}
