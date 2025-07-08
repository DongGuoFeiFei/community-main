package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 颜色枚举类 - 直接使用颜色名称
 * 包含社区网站常用的颜色定义
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/


@Getter
@AllArgsConstructor
public enum ColorEnum {
    // 基础颜色
    RED("#FF0000", "错误状态、警示、重要通知", "red"),
    GREEN("#00FF00", "成功状态、通过、可用", "green"),
    BLUE("#0000FF", "主色调、链接、可点击", "blue"),
    YELLOW("#FFFF00", "警告、注意、待处理", "yellow"),
    ORANGE("#FFA500", "次要警告、提醒", "orange"),
    PURPLE("#800080", "特殊标记、VIP标识", "purple"),
    PINK("#FFC0CB", "女性主题、爱心、点赞", "pink"),
    BROWN("#A52A2A", "复古风格、earthy tones", "brown"),

    // 中性色
    WHITE("#FFFFFF", "背景、文字反色", "white"),
    BLACK("#000000", "文字、边框", "black"),
    GRAY("#808080", "禁用状态、次要信息", "gray"),
    LIGHT_GRAY("#D3D3D3", "浅边框、背景", "lightgray"),
    DARK_GRAY("#A9A9A9", "深边框、强调文字", "darkgray"),

    // 现代UI常用色
    TEAL("#008080", "清新风格、水色主题", "teal"),
    NAVY("#000080", "深色主题、专业感", "navy"),
    LIME("#00FF00", "高亮、成功强调", "lime"),
    OLIVE("#808000", "自然、环保主题", "olive"),
    MAROON("#800000", "传统、正式场合", "maroon"),
    CYAN("#00FFFF", "科技感、突出显示", "cyan"),
    MAGENTA("#FF00FF", "艺术感、强调", "magenta"),

    // 社交媒体常用色
    FACEBOOK_BLUE("#1877F2", "Facebook品牌色", "facebookblue"),
    TWITTER_BLUE("#1DA1F2", "Twitter品牌色", "twitterblue"),
    INSTAGRAM_PURPLE("#E1306C", "Instagram品牌色", "instagrampurple"),
    YOUTUBE_RED("#FF0000", "YouTube品牌色", "youtubered"),
    LINKEDIN_BLUE("#0077B5", "LinkedIn品牌色", "linkedinblue"),

    // 特殊用途
    TRANSPARENT("rgba(0,0,0,0)", "透明背景", "transparent");

    private final String hexCode;
    private final String description;
    private final String color; // 小写形式的颜色名称

    /**
     * 根据小写颜色名称查找枚举
     */
    public static ColorEnum fromColorName(String colorName) {
        for (ColorEnum color : ColorEnum.values()) {
            if (color.color.equalsIgnoreCase(colorName)) {
                return color;
            }
        }
        throw new IllegalArgumentException("未知的颜色名称: " + colorName);
    }
}
