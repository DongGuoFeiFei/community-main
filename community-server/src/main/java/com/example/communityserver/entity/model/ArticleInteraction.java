package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.communityserver.entity.enums.ArticleInteractionTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章互动记录实体类
 */
@Data
@TableName("article_interaction")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文章互动记录")
public class ArticleInteraction {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "互动记录ID", example = "1")
    private Long id;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID", example = "1001")
    private Long articleId;

    /**
     * 用户ID（null表示未登录用户）
     */
    @ApiModelProperty(value = "用户ID", example = "1001")
    private Long userId;

    /**
     * 互动类型
     */
    @ApiModelProperty(value = "互动类型", example = "")
    private ArticleInteractionTypeEnum actionType;

    /**
     * 互动时间
     */
    @ApiModelProperty(value = "互动时间", example = "2023-10-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;

    /**
     * 浏览者IP地址
     */
    @ApiModelProperty(value = "IP地址", example = "192.168.1.1")
    private String ipAddress;

    /**
     * 设备信息
     */
    @ApiModelProperty(value = "设备信息", example = "Mozilla/5.0 (Windows NT 10.0...)")
    private String deviceInfo;

    /**
     * 额外数据（JSON格式，如评论内容等）
     */
    @ApiModelProperty(value = "额外数据(JSON格式)")
    private String extraData;

    /**
     * @Description: 创建新互动需要数据
     * @Param: [articleId, userId（非必须）, actionType, extraData（非必须）]
     * @return:
     * @Author: DongGuo
     */

    public ArticleInteraction(Long articleId, Long userId, ArticleInteractionTypeEnum actionType, String extraData) {
        this.articleId = articleId;
        this.userId = userId;
        this.actionType = actionType;
        this.extraData = extraData;
    }
}