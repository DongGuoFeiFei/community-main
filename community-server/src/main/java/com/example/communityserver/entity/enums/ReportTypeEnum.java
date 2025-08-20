package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 举报类型和结果
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/
@Getter
@AllArgsConstructor
public enum ReportTypeEnum {
    // 举报原因
    AD(1, "垃圾广告"),
    PORNOGRAPHY(2, "色情低俗"),
    POLITICAL_SENSITIVE(3, "虚假信息"),
    INFRINGEMENT(4, "侵权"),
    OTHER(5, "其他"),

    // 举报处理结果'处理结果(1:删除内容,2:警告用户,3:封禁用户,4:无违规)'
    DELETE_CONTENT(6, "删除内容"),
    WARN_USER(7, "警告用户"),
    BAN_USER(8, "封禁用户"),
    NO_VIOLATION(9, "无违规");
    private static final Map<Integer, ReportTypeEnum> KEY_MAP = new HashMap<>();

    static {
        for (ReportTypeEnum type : values()) {
            KEY_MAP.put(type.getKey(), type);
        }
    }

    private final Integer key;
    private final String label;

    public static ReportTypeEnum fromKey(Integer key) {
        ReportTypeEnum type = KEY_MAP.get(key);
        if (type == null) {
            throw new IllegalArgumentException("No enum constant with key: " + key);
        }
        return type;
    }
}
